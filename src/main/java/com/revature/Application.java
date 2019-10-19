package com.revature;

import com.revature.views.MainMenu;
import com.revature.views.View;

public class Application {
		
	public static void main(String[] args) {
		View currentView = new MainMenu();
		
		while (currentView != null) {
			currentView = currentView.run();
		}
		
		System.out.println("Thank you for choosing Gosbank");
	}
}
