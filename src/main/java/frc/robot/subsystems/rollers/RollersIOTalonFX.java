package frc.robot.subsystems.rollers;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

public class RollersIOTalonFX implements RollersIO{
    private final TalonFX rollerTalonFX = new TalonFX(RollersConstants.rollerID);
    private final VoltageOut voltageOut = new VoltageOut(0.0);

    @Override
    public void updateInputs(RollersIOInputs inputs) {
       inputs.motorSpeed = rollerTalonFX.getVelocity() .getValueAsDouble();
       inputs.statorCurrent = rollerTalonFX.getStatorCurrent().getValueAsDouble();
       inputs.supplyCurrent = rollerTalonFX.getSupplyCurrent().getValueAsDouble();
       inputs.rollersVolts = rollerTalonFX.getMotorVoltage().getValueAsDouble();   
    }

    @Override
    public void runVolts(double volts){
        voltageOut.Output = volts;
        rollerTalonFX.setControl(voltageOut);
      }
}
