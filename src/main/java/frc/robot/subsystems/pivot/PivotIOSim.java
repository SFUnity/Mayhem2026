package frc.robot.subsystems.pivot;

public class PivotIOSim implements PivotIO {
  double pivotVolts = 0.0;
  double pivotSupplyCurrent = 0.0;
  double pivotStatorCurrent = 0.0;
  double pivotSpeed = 0.0;
  double pivotDegrees = 0.0;

  @Override
  public void updateInputs(PivotIOInputs inputs) {
    inputs.pivotVolts = this.pivotVolts;
    inputs.pivotSupplyCurrent = this.pivotSupplyCurrent;
    inputs.pivotStatorCurrent = this.pivotStatorCurrent;
    inputs.pivotSpeed = this.pivotSpeed;
    inputs.pivotDegrees = this.pivotDegrees;
  }

  @Override
  public void setMotorAngle(double degrees) {
    this.pivotDegrees = degrees;
  }

  @Override
  public void setEncoderAngle(double degrees) {
    this.pivotDegrees = degrees;
  }

  @Override
  public void resetEncoderAngle() {
    setEncoderAngle(PivotConstants.minAngleDegs);
  }
}
