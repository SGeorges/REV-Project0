create table personal_accounts (
	id serial primary key,
	full_name VARCHAR(19) not null,
	password VARCHAR not null,
	start_date date default(current_date),
	privileged boolean not null default('false')
);

create table accounts (
	id INTEGER primary key,
	amount MONEY default(0) not null,
	account_type VARCHAR(60) not null
);

create table account_access (
	personal_account_id INTEGER references personal_accounts(id),
	account_id INTEGER references accounts(id), 
	primary_account BOOLEAN default('true')
);

-- --------------------------  ROLE GENERATION  --------------------------
-- Creating privileges on tables and utilizing environment variables 

create role bank_manager LOGIN password 'password';

grant all privileges on all tables in schema public to bank_manager;
grant all privileges on all sequences in schema public to bank_manager;

grant delete on all tables in schema public to bank_manager;

 --		----- DISTRIBUTE WEALTH FUNCTION -----
create or replace function distribute_wealth(deposit_val money, acc_id integer, depositor_id integer)
returns MONEY as $$
	declare 
		counter INTEGER := 0;
		num_ppl INTEGER := (select COUNT(*) from (select COUNT(personal_account_id) from account_access 
							left join personal_accounts on personal_accounts.id = account_access.personal_account_id group by personal_account_id) as a);
		ppl_array integer[] = (select ARRAY(select personal_account_id as account_id from account_access group by personal_account_id));
		p_id INTEGER;
		a_id INTEGER;
	
	begin
	
		while counter <= num_ppl loop
			if (ppl_array[counter] = depositor_id) then
				p_id := depositor_id;
				a_id := acc_id;
			else
				p_id := ppl_array[counter];
					
				a_id := (select MIN(account_id) from account_access where personal_account_id = p_id and primary_account = 'true' );
				
				if (a_id is null) then
					a_id := (select MIN(account_id) from account_access where personal_account_id = p_id);
				end if;
			end if;
		
			update accounts set (amount) = (accounts.amount + deposit_val/num_ppl) where id = a_id;
			
			counter := counter + 1;
		end loop;
	
		return (deposit_val/num_ppl);
	end; $$ language plpgsql;
	
 --		----- TRANSFER FUNCTION -----
 
create or replace function transfer_wealth(transfer_val money, acc_in integer, acc_out integer)
returns boolean as $$
	begin 
		if EXISTS(select id from accounts where id = acc_in) then
			update accounts set (amount) = (accounts.amount + transfer_val) where id = acc_in;
			update accounts set (amount) = (accounts.amount - transfer_val) where id = acc_out;		
			return true;
		else
			return false;
		end if;

	end; $$ language plpgsql;
	
 --		----- CREATE NEW ACCOUNT FUNCTION -----
 
create or replace function create_account(user_id integer, acc_type varchar)
returns integer as $$
	declare
		acc_num INTEGER := (select random()*999999999);
		unique_num BOOLEAN := 'false';
	begin 
		-- checking if the account number is unique -- 
		while not unique_num loop
			if exists (select id from accounts where id = acc_num) then
				acc_num := (select random()*999999999);
			else 
				unique_num := 'true';
			end if;
		end loop;
	
		insert into accounts(id, account_type) values (acc_num, acc_type);
		insert into account_access(personal_account_id, account_id, primary_account) values (user_id, acc_num, 'true');
	
		return acc_num;
	end; $$ language plpgsql;
	
 --		----- ADD NEW JOIN FUNCTION -----
 
create or replace function add_holder(personal_acc_id integer, account_id integer)
returns void as $$

	begin 
		if exists (select id from personal_accounts where id = personal_acc_id) then
			insert into account_access(personal_account_id, account_id, primary_account) values
				(personal_acc_id, account_id, 'true');
		end if;
	end; $$ language plpgsql;