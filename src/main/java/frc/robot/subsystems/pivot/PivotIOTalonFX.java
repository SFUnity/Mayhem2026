package frc.robot.subsystems.pivot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.math.util.Units;


public class PivotIOTalonFX implements PivotIO {
    private final TalonFX pivot;
    private final TalonFXConfiguration config;

    public PivotIOTalonFX() {
        pivot = new TalonFX(PivotConstants.motorID);
        config = new TalonFXConfiguration();
        config.CurrentLimits.SupplyCurrentLimit = PivotConstants.supplyCurrentLimit;
        config.CurrentLimits.SupplyCurrentLimitEnable = true;
        pivot.getConfigurator().apply(config);
    }
    // Implement the methods for TalonFX hardware here
    @Override
    public void updateInputs(PivotIOInputs inputs) {
        inputs.pivotVolts = pivot.getMotorVoltage().getValueAsDouble();
        inputs.pivotSpeed = pivot.getRotorVelocity().getValueAsDouble();
        inputs.pivotDegrees = Units.rotationsToDegrees(pivot.getPosition().getValueAsDouble());
        inputs.pivotSupplyCurrent = pivot.getSupplyCurrent().getValueAsDouble();
        inputs.pivotStatorCurrent = pivot.getStatorCurrent().getValueAsDouble();
    }



    @Override
    public void setMotorAngle(double degrees) {
        // Set the motor angle using TalonFX
    }

    @Override
    public void setEncoderAngle(double degrees) {
        // Set the encoder angle using TalonFX
    }

    @Override
    public void resetEncoderAngle() {
        // Reset the encoder angle using TalonFX
    }

}
