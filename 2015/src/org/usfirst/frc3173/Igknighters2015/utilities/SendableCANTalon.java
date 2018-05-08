package org.usfirst.frc3173.Igknighters2015.utilities;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class SendableCANTalon extends CANTalon implements LiveWindowSendable {

	private ITable m_table;
	private ITableListener m_table_listener;

	public SendableCANTalon(int deviceNumber) {
		super(deviceNumber);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initTable(ITable subtable) {
		// TODO Auto-generated method stub
		m_table=subtable;
		updateTable();
	}

	@Override
	public ITable getTable() {
		// TODO Auto-generated method stub
		return m_table;
	}

	@Override
	public String getSmartDashboardType() {
		// TODO Auto-generated method stub
		return "Speed Controller";
	}

	@Override
	public void updateTable() {
		// TODO Auto-generated method stub
		if (m_table != null) {
            //TODO: "this is bad" - wpi DoubleSolenoid class 2015
            m_table.putNumber("Value", super.get());
        }
	}

	@Override
	public void startLiveWindowMode() {
		// TODO Auto-generated method stub
		set(0); // Stop for safety
		m_table_listener = new ITableListener() {
			@Override
			public void valueChanged(ITable itable, String key, Object value, boolean bln) {
				set(((Double) value).doubleValue());
			}
		};
		m_table.addTableListener("Value", m_table_listener, true);
	}

	@Override
	public void stopLiveWindowMode() {
		// TODO Auto-generated method stub
		set(0); // Stop for safety
		// TODO: Broken, should only remove the listener from "Value" only.
		m_table.removeTableListener(m_table_listener);
	}

}
