package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Trip {
	
	private String trip_id;
    private Bike bike;
    private Customer customer;
    private LocalDateTime trip_start;
    private LocalDateTime trip_end;
    private int tt_mod;
    private int tbill;
    private boolean activeFlag;
    
    public String getTrip_Id() {
    	return this.trip_id;
    }
	
    public Bike getBike() {
    	return this.bike;
    }
    
    public Customer getCustomer() {
    	return this.customer;
    }
    
    public LocalDateTime getTrip_Start() {
    	return this.trip_start;
    }   
    
    public LocalDateTime getTrip_End() {
    	return this.trip_end;
    }
    
    public int getTT_Mod() {
    	return this.tt_mod;
    }
    
    public boolean getActive_Flag() {
    	return this.activeFlag;
    }
    
    public void setTrip_Id( String trip_id) {
    	this.trip_id = trip_id;
    }
	
    public void setBike_Id(Bike bike) {
    	this.bike= bike;
    }
    
    public void setCustomer_Id(Customer customer) {
    	this.customer = customer;
    }
    
    public void setTrip_Start(LocalDateTime trip_start) {
    	this.trip_start = trip_start;
    }   
    
    public void setTrip_End(LocalDateTime trip_end) {
    	this.trip_end = trip_end;
    }
    
    public void setTT_Mod(int tt_mod) {
    	this.tt_mod = tt_mod;
    }
    
    public void setActive_Flag(boolean aFlag) {
    	this.activeFlag = aFlag;
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
					if (resultSetDockInfo.next()) {
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
					billReceipt = "Error: Unlock code is expired.";
				}
			} else {
				billReceipt = "Error: Unlock code is not found.";
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

}
