package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Dock {
	private String dock_id;
    private UnlockCode unlock_code;
    private Bike bike; 
    
    public Dock(String dock_id) {
    	this.dock_id = dock_id;
    	this.unlock_code = null;
    	this.bike = null;
    }
    
    public Dock(String dock_id, Bike bk) {
    	this.dock_id = dock_id;
    	this.unlock_code = null;
    	this.bike = bk;
    }
    
    public String getDock_Id() {
    	return this.dock_id;
    }
    
    public UnlockCode getUnlock_Code() {
    	return this.unlock_code;
    }
    
    public Bike getBike() {
    	return this.bike;
    }
    
    public void setDock_Id(String dock_id) {
    	this.dock_id = dock_id;
    }
    
    public void setUnlock_Code(UnlockCode uc_code) {
    	this.unlock_code = uc_code;
    }
    
    public void setBike(Bike bk) {
    	this.bike = bk;
    }
    
    public static ArrayList<Dock> getAvailable(Customer cust, String area_code, Connection con) {
		try {
			if (cust != null) {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(
						"select * from stations INNER JOIN docks ON stations.dock_id = docks.dock_id INNER JOIN bikes on docks.bike_id= bikes.bike_id where stations.station_code='"
								+ area_code + "' and docks.bike_id <> 'UNASSIGNED'");
				ArrayList<Dock> available = new ArrayList<Dock>();
				while (rs.next()) {
					Bike bk = new Bike(rs.getString(5), rs.getString(7));
					Dock dk = new Dock(rs.getString(2), bk);
					available.add(dk);
				}
				return available;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static ArrayList<Dock> getAvailableDocksForRent(String area_code, Connection con){
		try {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from stations INNER JOIN docks ON stations.dock_id = docks.dock_id INNER JOIN bikes on docks.bike_id= bikes.bike_id where stations.station_code='"+ area_code +"' and docks.bike_id IS NOT NULL");
			ArrayList<Dock> available = new ArrayList<>();
			while(rs.next()) {
				Bike bk = new Bike(rs.getString(5), rs.getString(7));
				Dock dk = new Dock(rs.getString(2) , bk);
				available.add(dk);
			}
			return available;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static ArrayList<Dock> getAvailableDocksForReturn(String area_code, Connection con){
		try {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from stations INNER JOIN docks ON stations.dock_id = docks.dock_id where stations.station_code='"+ area_code +"' and docks.bike_id IS NULL");
			ArrayList<Dock> available = new ArrayList<>();
			while(rs.next()) {
				Dock dk = new Dock(rs.getString(2) , null);
				available.add(dk);
			}
			return available;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	private static int codeGenerator() {
		int min = 1;
		int max = 3;
		int code = (int) Math.floor(Math.random() * (max - min + 1) + min);

		for (int i = 0; i < 4; i++) {
			int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
			code = code * 10 + random_int;
		}
		return code;
	}

	public static UnlockCode issueUnlockCode(Dock dk, Connection con) {
		String stationCode = getStationCodeForDock(dk, con);
		Set<Integer> existingCodes = new HashSet<>();

		if (!stationCode.equals("")) {
			existingCodes = getExitingUnlockCodes(stationCode, con);
		}

		int code;
		do {
			code = codeGenerator();
		} while (existingCodes.contains(code));
		LocalDateTime strt = LocalDateTime.now();
		UnlockCode uc = new UnlockCode(code, strt);

		String query = "INSERT into unlock_codes VALUES(?, ? )";
		String query2 = "UPDATE docks SET unlock_code = ? where dock_id=?";
		try {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setObject(2, uc.getStart());
			preparedStatement.setObject(1, uc.getUnlock_Code());
			preparedStatement.executeUpdate();

			PreparedStatement preparedStatement2 = con.prepareStatement(query2);
			preparedStatement2.setObject(1, uc.getUnlock_Code());
			preparedStatement2.setObject(2, dk.getDock_Id());
			preparedStatement2.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		return uc;
	}
	
	private static String getStationCodeForDock(Dock dk, Connection con) {
		String stationCode = "";

		try {
			Statement statement = con.createStatement();
			String sql = "SELECT station_code FROM stations INNER JOIN docks ON stations.dock_id = docks.dock_id WHERE docks.dock_id='"+ dk.getDock_Id() +"'";
			ResultSet resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				stationCode = resultSet.getString(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			return stationCode;
		}
	}

	private static Set<Integer> getExitingUnlockCodes(String stationCode, Connection con) {
		Set<Integer> existingCodes = new HashSet<>();

		try {
			Statement statement = con.createStatement();
			String sql = "SELECT unlock_code FROM docks INNER JOIN stations ON stations.dock_id = docks.dock_id WHERE stations.station_code='"+ stationCode +"'";
			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				String code = resultSet.getString(1);

				if (code != null) existingCodes.add(Integer.parseInt(code));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			return existingCodes;
		}
	}
}
