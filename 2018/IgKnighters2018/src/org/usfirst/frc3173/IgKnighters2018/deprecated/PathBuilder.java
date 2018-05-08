package org.usfirst.frc3173.IgKnighters2018.deprecated;

import java.util.ArrayList;

import org.usfirst.frc3173.IgKnighters2018.commandgroups.AutonomousPath;
import org.usfirst.frc3173.IgKnighters2018.utilities.Constants;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class PathBuilder {
	
	public static enum AutonomousAction {
		NOTHING, SWITCH, SCALE;
	};
	
	private ArrayList<Vector2> autonomousPath = new ArrayList<Vector2>();
	//first index in the path is the starting position
	private float exitAngle;
	private AutonomousAction exitAction;
	
	public PathBuilder(String gameData, String dsPosition, String autoProtocol) {
		//insert builder
		if (dsPosition.equals(Constants.DRIVERSTATION_ONE_STRING)) {
			
			autonomousPath.add(FieldPositions.DRIVERSTATION_ONE);
			
			if (autoProtocol.equals(Constants.AUTONOMOUS_PROTOCOL_PASS_LINE)) {
				
				autonomousPath.add(FieldPositions.LAYER_ONE_ID_TWO);
				autonomousPath.add(FieldPositions.LAYER_ONE_ID_ONE);
				autonomousPath.add(FieldPositions.LAYER_TWO_ID_ONE);
				exitAction = AutonomousAction.NOTHING;
				exitAngle = 0f;
				
			} else if (autoProtocol.equals(Constants.AUTONOMOUS_PROTOCOL_SWITCH)) {
				
				autonomousPath.add(FieldPositions.LAYER_ONE_ID_TWO);
				autonomousPath.add(FieldPositions.LAYER_ONE_ID_ONE);
				autonomousPath.add(FieldPositions.SWITCH_WALL_INTERMEDIATE_LEFT);
				switch (gameData.charAt(0)) {
				case 'l':
				case 'L':
					autonomousPath.add(FieldPositions.SWITCH_DISTAL_LEFT);
					exitAngle = 90f;
					break;
				case 'r':
				case 'R':
					autonomousPath.add(FieldPositions.LAYER_TWO_ID_ONE);
					autonomousPath.add(FieldPositions.LAYER_TWO_ID_FIVE);
					autonomousPath.add(FieldPositions.SWITCH_WALL_INTERMEDIATE_RIGHT);
					autonomousPath.add(FieldPositions.SWITCH_DISTAL_RIGHT);
					exitAngle = -90f;
					break;
				}
				
				exitAction = AutonomousAction.SWITCH;
				
				} else if (autoProtocol.equals(Constants.AUTONOMOUS_PROTOCOL_SCALE)) {
					
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_TWO);
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_ONE);
					autonomousPath.add(FieldPositions.LAYER_TWO_ID_ONE);
					switch(gameData.charAt(1)) {
					case 'l':
					case 'L':
						autonomousPath.add(FieldPositions.SCALE_LEFT);
						exitAngle = 90f;
						break;
					case 'r':
					case 'R':
						autonomousPath.add(FieldPositions.LAYER_TWO_ID_FIVE);
						autonomousPath.add(FieldPositions.SCALE_RIGHT);
						exitAngle = -90f;
						break;
					}
					
					exitAction = AutonomousAction.SCALE;
					
				}
		} else if (dsPosition.equals(Constants.DRIVERSTATION_TWO_STRING)) {
			
			autonomousPath.add(FieldPositions.DRIVERSTATION_TWO);
			
			if (autoProtocol.equals(Constants.AUTONOMOUS_PROTOCOL_PASS_LINE)) {
				
				switch(gameData.charAt(1)) {
				case 'l':
				case 'L':
					autonomousPath.add(FieldPositions.DRIVERSTATION_ONE);
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_TWO);
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_ONE);
					autonomousPath.add(FieldPositions.LAYER_TWO_ID_ONE);
					break;
				case 'r':
				case 'R':
					autonomousPath.add(FieldPositions.DRIVERSTATION_THREE);
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_FOUR);
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_FIVE);
					autonomousPath.add(FieldPositions.LAYER_TWO_ID_FIVE);
					break;
				}
				
				exitAction = AutonomousAction.NOTHING;
				exitAngle = 0f;
				
			} else if (autoProtocol.equals(Constants.AUTONOMOUS_PROTOCOL_SWITCH)) {
				
				switch(gameData.charAt(0)) {
				case 'l':
				case 'L':
					autonomousPath.add(FieldPositions.DRIVERSTATION_ONE);
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_TWO);
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_ONE);
					autonomousPath.add(FieldPositions.SWITCH_WALL_INTERMEDIATE_LEFT);
					autonomousPath.add(FieldPositions.SWITCH_DISTAL_LEFT);
					exitAngle = 90f;
					break;
				case 'r':
				case 'R':
					autonomousPath.add(FieldPositions.DRIVERSTATION_THREE);
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_FOUR);
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_FIVE);
					autonomousPath.add(FieldPositions.SWITCH_WALL_INTERMEDIATE_RIGHT);
					autonomousPath.add(FieldPositions.SWITCH_DISTAL_RIGHT);
					exitAngle = -90f;
					break;
				}
				
				exitAction = AutonomousAction.SWITCH;
				
			} else if (autoProtocol.equals(Constants.AUTONOMOUS_PROTOCOL_SCALE)) {
				
				switch(gameData.charAt(1)) {
				case 'l':
				case 'L':
					autonomousPath.add(FieldPositions.DRIVERSTATION_ONE);
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_TWO);
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_ONE);
					autonomousPath.add(FieldPositions.LAYER_TWO_ID_ONE);
					autonomousPath.add(FieldPositions.SCALE_LEFT);
					exitAngle = 90f;
					break;
				case 'r':
				case 'R':
					autonomousPath.add(FieldPositions.DRIVERSTATION_THREE);
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_FOUR);
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_FIVE);
					autonomousPath.add(FieldPositions.LAYER_TWO_ID_FIVE);
					autonomousPath.add(FieldPositions.SCALE_RIGHT);
					exitAngle = -90f;
					break;
					
				}
				
			}
			
		} else if (dsPosition.equals(Constants.DRIVERSTATION_THREE_STRING)) {
			
			autonomousPath.add(FieldPositions.DRIVERSTATION_THREE);
			
			if (autoProtocol.equals(Constants.AUTONOMOUS_PROTOCOL_PASS_LINE)) {
				
				autonomousPath.add(FieldPositions.LAYER_ONE_ID_FOUR);
				autonomousPath.add(FieldPositions.LAYER_ONE_ID_FIVE);
				autonomousPath.add(FieldPositions.LAYER_TWO_ID_FIVE);
				exitAngle = 0f;
				exitAction = AutonomousAction.NOTHING;
				
			} else if (autoProtocol.equals(Constants.AUTONOMOUS_PROTOCOL_SWITCH)) {
				
				autonomousPath.add(FieldPositions.LAYER_ONE_ID_FOUR);
				autonomousPath.add(FieldPositions.LAYER_ONE_ID_FIVE);
				switch (gameData.charAt(0)) {
				case 'l':
				case 'L':
					autonomousPath.add(FieldPositions.LAYER_TWO_ID_FIVE);
					autonomousPath.add(FieldPositions.LAYER_TWO_ID_ONE);
					autonomousPath.add(FieldPositions.SWITCH_WALL_INTERMEDIATE_LEFT);
					autonomousPath.add(FieldPositions.SWITCH_DISTAL_LEFT);
					exitAngle = 90f;
					break;
				case 'r':
				case 'R':
					autonomousPath.add(FieldPositions.SWITCH_WALL_INTERMEDIATE_RIGHT);
					autonomousPath.add(FieldPositions.SWITCH_DISTAL_RIGHT);
					exitAngle = -90f;
					break;
				}
				
				exitAction = AutonomousAction.SWITCH;
				
				} else if (autoProtocol.equals(Constants.AUTONOMOUS_PROTOCOL_SCALE)) {
					
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_TWO);
					autonomousPath.add(FieldPositions.LAYER_ONE_ID_ONE);
					autonomousPath.add(FieldPositions.LAYER_TWO_ID_ONE);
					switch(gameData.charAt(1)) {
					case 'l':
					case 'L':
						autonomousPath.add(FieldPositions.SCALE_LEFT);
						exitAngle = 90f;
						break;
					case 'r':
					case 'R':
						autonomousPath.add(FieldPositions.LAYER_TWO_ID_FIVE);
						autonomousPath.add(FieldPositions.SCALE_RIGHT);
						exitAngle = -90f;
						break;
					}
					
					exitAction = AutonomousAction.SCALE;
					
				}
			
		}
		Vector2[] array = new Vector2[autonomousPath.size()];
		for (int i = 0; i < autonomousPath.size(); i++) {
			array[i] = autonomousPath.get(i);
		}
		Scheduler.getInstance().add(new AutonomousPath(array, exitAngle, exitAction));
	}
	
	public Vector2[] getPath() {
		Vector2[] array = new Vector2[autonomousPath.size()];
		for (int i = 0; i < autonomousPath.size(); i++) {
			array[i] = autonomousPath.get(i);
		}
		return array;
	}
	
	public float getExitAngle() {
		return exitAngle;
	}
	
	public PathBuilder.AutonomousAction getExitAction() {
		return exitAction;
	}
}
