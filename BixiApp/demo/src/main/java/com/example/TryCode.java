package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TryCode {
	
	public static Customer login( String customer_id, String pwd, Connection con) {
		try {
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select pwd from auth where customer_id='"+ customer_id+"'");
			if (rs.next()) {
				if (rs.getString(1).equals(pwd)) {
					try  {
						Statement stmt2=con.createStatement();  
						ResultSet rs2=stmt.executeQuery("SELECT * from customers WHERE customer_id='"+customer_id+"'");
						rs2.next();
						String name = rs2.getString(2);
						int c_age = rs2.getInt(3);
						String c_mail = rs2.getString(4);
						int c_phone = rs2.getInt(5);
						int bill = rs2.getInt(6);
						
						Customer ct = new Customer(customer_id, name, c_age, c_mail, c_phone,bill);
						return ct;
			            
			        }
					catch(Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace(System.out);
						return null;
					}
					
				}
				else {
					
					return null;
				}
			} 
			else {
				return null;
			}
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
			
		}		
	}
	
	public static ArrayList<Dock> getAvailable(Customer cust, String area_code, Connection con ){
		try {
			if (cust != null) {
				Statement stmt=con.createStatement();  
				ResultSet rs=stmt.executeQuery("select * from stations INNER JOIN docks ON stations.dock_id = docks.dock_id INNER JOIN bikes on docks.bike_id= bikes.bike_id where stations.station_code='"+ area_code +"' and docks.bike_id <> 'UNASSIGNED'");
				ArrayList<Dock> available = new ArrayList<Dock>();
				while(rs.next()) {
					Bike bk = new Bike(rs.getString(5), rs.getString(7));
					Dock dk = new Dock(rs.getString(2) , bk);
					available.add(dk);					
				}
				return available;
			}
			else {
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}		
	}
	
	private static int codeGenerator() {
		int min = 1; 
	    int max = 3;
	    int code = (int)Math.floor(Math.random() * (max - min + 1) + min);
	    
	    for(int i=0;i<4;i++) {
	    	int random_int = (int)Math.floor(Math.random() * (max - min + 1) + min);
	    	code= code*10+random_int;
	    }
	    return code;
	}
	
	public static UnlockCode issueUnlockCode ( Customer cust,  Dock dk, Connection con ) {
		int code = codeGenerator();
		LocalDateTime strt = LocalDateTime.now(); 
		UnlockCode uc = new UnlockCode(code, strt);
		
		String query = "INSERT into unlock_codes VALUES(?, ? )";
		String query2 = "UPDATE docks SET unlock_code = ? where dock_id=?";
		try  {
			PreparedStatement preparedStatement = con.prepareStatement ( query ) ;
            preparedStatement.setObject ( 2, uc.getStart() );  
            preparedStatement.setObject ( 1, uc.getUnlock_Code() );       
            preparedStatement.executeUpdate ( );
            
            PreparedStatement preparedStatement2 = con.prepareStatement ( query2 ) ;
            preparedStatement2.setObject ( 1, uc.getUnlock_Code() );
            System.out.println(dk.getDock_Id());
            preparedStatement2.setObject ( 2, dk.getDock_Id() );            
            preparedStatement2.executeUpdate ( );
            
        }
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		return uc;		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{  
			  
			Connection con=DriverManager.getConnection( "jdbc:mysql://localhost:3306/tvm","root","sdm@23"); 
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from auth");
			Customer cust = login("CVER2345", "pwd2345", con);
			if (cust!=null) {
				System.out.println("login successfull");
				ArrayList<Dock> avl = getAvailable(cust, "ATW", con );
				if (avl!=null) {
					UnlockCode uc = issueUnlockCode ( cust,  avl.get(0), con );
					System.out.println(uc.getUnlock_Code());
				}
			}
			else {
				System.out.println("login unsuccessfull");
			}
			
			
			
		}catch(Exception e){ 
			e.printStackTrace(System.out);
			System.out.println(e);
		}  
			  

	}

}
