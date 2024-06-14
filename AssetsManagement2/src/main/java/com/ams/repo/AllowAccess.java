package com.ams.repo;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.ams.repo.Repo;

public class AllowAccess {
	
	public static void allowTheirOwnAccess(List<Integer> lk) throws SQLException {
		Scanner sc = new Scanner(System.in);
		
		boolean validChoice = true;
		int choice = 0;
		while(validChoice ) {
			System.out.print("Please choose which you want :");
			
			choice = sc.nextInt();
			if(lk.contains(choice)) {
				//System.out.println("you are allowed");
				switch(choice) {
				case 1 :
				{
					System.out.println("Choose Add Asset");
					//Actions.addAsset();
					validChoice = false;
					break;
				}
				case 2 :
				{
					System.out.println("Choose Change Asset");
					validChoice = false;
					break;
				}
				case 3 :
				{
					System.out.println("Choose View Asset");
					Repo.select("assets");
					validChoice = false;
					break;
				}
				case 4 :
				{
					System.out.println("Choose Remove Asset");
					validChoice = false;
					break;
				}
				case 5 :
				{
					System.out.println("Choose Buy New Asset");
					validChoice = false;
					break;
				}
				case 6 :
				{
					System.out.println("Choose full_access");
					validChoice = false;
					break;
				}
				case 7 :
				{
					System.out.println("Choose assign_assets");
					validChoice = false;
					break;
				}
				case 8 :
				{
					System.out.println("Choose view_logs");
					validChoice = false;
					break;
				}
				case 9 :
				{
					System.out.println("Choose view_assigned_assets");
					validChoice = false;
					break;
				}
				case 10 :
				{
					System.out.println("Choose update_asset_status");
					validChoice = false;
					break;
				}
				case 11 :
				{
					System.out.println("Choose request_assets");
					validChoice = false;
					break;
				}
				case 12 :
				{
					System.out.println("Choose approve_asset_requests");
					validChoice = false;
					break;
				}
				case 13 :
				{
					System.out.println("Choose view_request_status");
					validChoice = false;
					break;
				}
			
				
				default:
				{
					System.out.println("Please enter a valid choice\n");
					break;
				}
				}
			}
			else {
				System.out.println("you are not allow to this action . You can only choose bellow choices");
				System.out.println(lk);
			}
			
		}
		
	}
}
