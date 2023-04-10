package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.SQLException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.UUID;



public class TryCode {

	public static Customer login(String customer_id, String pwd, Connection con) {
		try {
			System.out.println(customer_id+"" + pwd);
			Class.forName("com.mysql.cj.jdbc.Driver");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select pwd from auth where customer_id='" + customer_id + "'");
			if (rs.next()) {
				if (rs.getString(1).equals(pwd)) {
					try {
						Statement stmt2 = con.createStatement();
						ResultSet rs2 = stmt
								.executeQuery("SELECT * from customers WHERE customer_id='" + customer_id + "'");
						rs2.next();
						String name = rs2.getString(2);
						int c_age = rs2.getInt(3);
						String c_mail = rs2.getString(4);
						int c_phone = rs2.getInt(5);
						int bill = rs2.getInt(6);

						Customer ct = new Customer(customer_id, name, c_age, c_mail, c_phone, bill);
						UserSession.getInstance(name,customer_id);
						return ct;

					} catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace(System.out);
						return null;
					}

				} else {

					return null;
				}
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;

		}
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

	// public static ArrayList<Dock> getAvailableDocksForReturn(String area_code, Connection con){
	// 	try {
	// 		Statement stmt=con.createStatement();
	// 		ResultSet rs=stmt.executeQuery("select * from stations INNER JOIN docks ON stations.dock_id = docks.dock_id where stations.station_code='"+ area_code +"' and docks.bike_id = 'UNASSIGNED'");
	// 		ArrayList<Dock> available = new ArrayList<>();
	// 		while(rs.next()) {				
	// 			Dock dk = new Dock(rs.getString("dock_id"));
	// 			available.add(dk);
	// 		}
	// 		return available;
	// 	}
	// 	catch(Exception e) {
	// 		e.printStackTrace(System.out);
	// 		return null;
	// 	}
	// }


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
		int code = codeGenerator();
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tvm", "root", "sdm@23");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from auth");
			Customer cust = login("CVER2345", "pwd2345", con);
			if (cust != null) {
				System.out.println("login successfull");
				ArrayList<Dock> avl = getAvailable(cust, "ATW", con);
				if (avl != null) {
					UnlockCode uc = issueUnlockCode(avl.get(0), con);
					System.out.println(uc.getUnlock_Code());
				}
			} else {
				System.out.println("login unsuccessfull");
			}

		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.out.println(e);
		}

	}
	public static String issueBike(String customerId, String userName ,int unlockCode, Connection con) {
		String getUnlockInfo = "SELECT * FROM unlock_codes WHERE unlock_code = ?";
		String getDockInfo = "SELECT * FROM docks WHERE unlock_code = ?";
		String billReceipt;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		try {
			PreparedStatement preparedStatementGetUnlockInfo = con.prepareStatement(getUnlockInfo);
			preparedStatementGetUnlockInfo.setInt(1, unlockCode);
			ResultSet resultSetUnlockInfo = preparedStatementGetUnlockInfo.executeQuery();

			if (resultSetUnlockInfo.next()) {
				LocalDateTime startTime = resultSetUnlockInfo.getTimestamp("start_time").toLocalDateTime();
				LocalDateTime currentTime = LocalDateTime.now();
				Duration duration = Duration.between(startTime, currentTime);
				long diffInMinutes = duration.toMinutes();

				if (diffInMinutes < 5) {
					PreparedStatement preparedStatementGetDockInfo = con.prepareStatement(getDockInfo);
					preparedStatementGetDockInfo.setInt(1, unlockCode);
					ResultSet resultSetDockInfo = preparedStatementGetDockInfo.executeQuery();
					resultSetDockInfo.next();
					String dockId = resultSetDockInfo.getString("dock_id");
					String bikeId = resultSetDockInfo.getString("bike_id");

					// Update dock
					String updateDockQuery = "UPDATE docks SET bike_id = NULL, unlock_code = NULL WHERE dock_id = ?";
					PreparedStatement preparedStatementUpdateDock = con.prepareStatement(updateDockQuery);
					preparedStatementUpdateDock.setString(1, dockId);
					preparedStatementUpdateDock.executeUpdate();

					// Insert entry in Trips table
					String insertTripQuery = "INSERT INTO trips (trip_id, bike_id, customer_id, trip_start, activeFlag) VALUES (?, ?, ?, ?, ?)";
					String tripId = "T" + System.currentTimeMillis();
					PreparedStatement preparedStatementInsertTrip = con.prepareStatement(insertTripQuery);
					preparedStatementInsertTrip.setString(1, tripId);
					preparedStatementInsertTrip.setString(2, bikeId);
					preparedStatementInsertTrip.setString(3, customerId);
					preparedStatementInsertTrip.setObject(4, currentTime);
					preparedStatementInsertTrip.setBoolean(5, true);
					preparedStatementInsertTrip.executeUpdate();

					billReceipt = "Bill Receipt:\n" +
							"Customer ID: " + customerId + "\n" +
							"Name: " + userName + "\n" +
							"Bike ID: " + bikeId + "\n" +
							"Unlock Code: " + unlockCode + "\n" +
							"Start Time: " + currentTime.format(formatter) + "\n" +
							"Happy Riding!";
				} else {
					billReceipt = "Error: Unlock code is invalid.";
				}
			} else {
				billReceipt = "Error: Unlock code not found.";
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			billReceipt = "Error: Unable to process the request.";
		}

		return billReceipt;
	}


	public static int returnAction(String customer_id, String area_code, Connection con) {
		ArrayList<Dock> adk = getAvailableDocksForReturn(area_code, con);
		if ((adk==null) || (adk.isEmpty())) {
			boolean extendFlag = extendTime(customer_id, con);
			if (extendFlag==true) {
				return 1;
			}
			else {
				return -1;
			}
		}
		else {
			return 2;
		}
		
	}
	
	public static boolean extendTime(String customer_id, Connection con) {
		
		String getActiveTrip = "SELECT trip_id, trip_start FROM trips WHERE customer_id = ? AND activeFlag = ?";
		String updateTripStart = "UPDATE trips SET trip_start = ? WHERE trip_id = ?";

		try {		

			if (false) {
				return false;
			} else {
				PreparedStatement preparedStatementGetActiveTrip = con.prepareStatement(getActiveTrip);
				preparedStatementGetActiveTrip.setString(1, customer_id);
				preparedStatementGetActiveTrip.setBoolean(2, true);
				ResultSet resultSetActiveTrip = preparedStatementGetActiveTrip.executeQuery();

				if (resultSetActiveTrip.next()) {
					String tripId = resultSetActiveTrip.getString("trip_id");
					LocalDateTime tripStart = resultSetActiveTrip.getTimestamp("trip_start").toLocalDateTime();
					LocalDateTime newTripStart = tripStart.minus(Duration.ofMinutes(15));

					PreparedStatement preparedStatementUpdateTripStart = con.prepareStatement(updateTripStart);
					preparedStatementUpdateTripStart.setObject(1, newTripStart);
					preparedStatementUpdateTripStart.setString(2, tripId);
					preparedStatementUpdateTripStart.executeUpdate();

					//return "Trip time extended successfully.";
					return true;
				} else {
					return false;
					//return "Error: No active trip found for the customer.";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			return false;
			//return "Error: Unable to process the request.";
		}
	}

	public static float returnBike(String customer_id, String dockId, Connection con) {
		String getTripInfo = "SELECT trip_start, bike_id FROM trips WHERE customer_id = ? AND activeFlag = ?";
		String updateDock = "UPDATE docks SET bike_id = ? WHERE dock_id = ?";
		
		String setTripEnd = "UPDATE trips SET trip_end = ?, activeFlag = ? WHERE bike_id = ? AND customer_id = ? AND activeFlag = ?";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		try {
			

			PreparedStatement preparedStatementGetTripInfo = con.prepareStatement(getTripInfo);
			//preparedStatementGetTripInfo.setString(1, bikeId);
			preparedStatementGetTripInfo.setString(1,customer_id);
			preparedStatementGetTripInfo.setBoolean(2, true);
			ResultSet resultSet = preparedStatementGetTripInfo.executeQuery();

			if (resultSet.next()) {
				LocalDateTime tripStart = resultSet.getTimestamp("trip_start").toLocalDateTime();
				LocalDateTime tripEnd = LocalDateTime.now();
				
				PreparedStatement preparedStatementUpdateDock = con.prepareStatement(updateDock);
				preparedStatementUpdateDock.setString(1, resultSet.getString("bike_id"));
				preparedStatementUpdateDock.setString(2, dockId);
				preparedStatementUpdateDock.executeUpdate();

				PreparedStatement preparedStatementSetTripEnd = con.prepareStatement(setTripEnd);
				preparedStatementSetTripEnd.setObject(1, tripEnd);
				preparedStatementSetTripEnd.setBoolean(2, false);
				preparedStatementSetTripEnd.setString(3, resultSet.getString("bike_id"));
				preparedStatementSetTripEnd.setString(4, customer_id);
				preparedStatementSetTripEnd.setBoolean(5, true);
				preparedStatementSetTripEnd.executeUpdate();

				Duration duration = Duration.between(tripStart, tripEnd);
				long minutes = duration.toMinutes();
				float tripCost = (float) (1.25+ 0.15 * minutes);

				return tripCost;
			} else {
				System.out.println("Error: No active trip found for the given bike and customer.");
				return -1;
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			System.out.println("Error: Unable to process the request.");
			return -1;
		}
	}
	// public static String extendTime(String customerId, String stationNo, String dockingId, Connection con) {
	// 	String checkDocks = "SELECT COUNT(*) as dock_count FROM stations WHERE station_code = ? AND dock_id NOT IN (SELECT dock_id FROM docks WHERE bike_id IS NULL)";
	// 	String getActiveTrip = "SELECT trip_id, trip_start FROM trips WHERE customer_id = ? AND activeFlag = ?";
	// 	String updateTripStart = "UPDATE trips SET trip_start = ? WHERE trip_id = ?";

	// 	try {
	// 		PreparedStatement preparedStatementCheckDocks = con.prepareStatement(checkDocks);
	// 		preparedStatementCheckDocks.setString(1, stationNo);
	// 		ResultSet resultSet = preparedStatementCheckDocks.executeQuery();

	// 		resultSet.next();
	// 		int dockCount = resultSet.getInt("dock_count");

	// 		if (dockCount == 0) {
	// 			return "Error: Some docks in the station are empty.";
	// 		} else {
	// 			PreparedStatement preparedStatementGetActiveTrip = con.prepareStatement(getActiveTrip);
	// 			preparedStatementGetActiveTrip.setString(1, customerId);
	// 			preparedStatementGetActiveTrip.setBoolean(2, true);
	// 			ResultSet resultSetActiveTrip = preparedStatementGetActiveTrip.executeQuery();

	// 			if (resultSetActiveTrip.next()) {
	// 				String tripId = resultSetActiveTrip.getString("trip_id");
	// 				LocalDateTime tripStart = resultSetActiveTrip.getTimestamp("trip_start").toLocalDateTime();
	// 				LocalDateTime newTripStart = tripStart.minus(Duration.ofMinutes(15));

	// 				PreparedStatement preparedStatementUpdateTripStart = con.prepareStatement(updateTripStart);
	// 				preparedStatementUpdateTripStart.setObject(1, newTripStart);
	// 				preparedStatementUpdateTripStart.setString(2, tripId);
	// 				preparedStatementUpdateTripStart.executeUpdate();

	// 				return "Trip time extended successfully.";
	// 			} else {
	// 				return "Error: No active trip found for the customer.";
	// 			}
	// 		}
	// 	} catch (SQLException e) {
	// 		e.printStackTrace(System.out);
	// 		return "Error: Unable to process the request.";
	// 	}
	// }

}