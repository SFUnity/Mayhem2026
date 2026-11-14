package frc.robot.subsystems.pivot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;

import edu.wpi.first.math.util.Units;

public class PivotIOTalonFX implements PivotIO {
    private final TalonFX pivot;
    private final TalonFXConfiguration config;

    private final MotionMagicVoltage motionMagicVoltage = new MotionMagicVoltage(0.0);

    public PivotIOTalonFX() {
        pivot = new TalonFX(PivotConstants.motorID);
        config = new TalonFXConfiguration();
        config.CurrentLimits.SupplyCurrentLimit = PivotConstants.supplyCurrentLimit;
        config.CurrentLimits.SupplyCurrentLimitEnable = true;
        config.Feedback.SensorToMechanismRatio = PivotConstants.gearRatio;
        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine; 
        config.Slot0.kP = PivotConstants.kP;
        config.Slot0.kD = PivotConstants.kD;
        config.MotionMagic.MotionMagicAcceleration = PivotConstants.acceleration; 
        config.MotionMagic.MotionMagicCruiseVelocity = PivotConstants.velocity; 

        pivot.getConfigurator().apply(config);
    }
    // Implement the methods for TalonFX hardware here
    @Override
    public void updateInputs(PivotIOInputs inputs) {

    }

    @Override
    public void setMotorAngle(double degrees) {
        motionMagicVoltage.withPosition(Units.degreesToRotations(degrees));
        pivot.setControl(motionMagicVoltage);
        // Set the motor angle using TalonFX
    }

    @Override
    public void setEncoderAngle(double degrees) {
        pivot.setPosition(Units.degreesToRotations(degrees));
        // Set the encoder angle using TalonFX
    }

    @Override
    public void resetEncoderAngle() {
        setEncoderAngle(PivotConstants.minAngleDegs);
        // Reset the encoder angle using TalonFX
    }



}
