package frc.robot.subsystems.rollers;

public class RollersIOSim implements RollersIO {
  private double rollersVolts = 0.0;
  private double rollersStatorCurrent = 0.0;
  private double rollerSupplyCurrent = 0.0;
  private double rollerSpeed = 0.0;

  public RollersIOSim() {}

  @Override
  public void updateInputs(RollersIOInputs inputs) {
    inputs.rollerSpeed = this.rollerSpeed;
    inputs.rollersVolts = this.rollersVolts;
    inputs.rollersStatorCurrent = this.rollersStatorCurrent;
    inputs.rollersSupplyCurrent = this.rollerSupplyCurrent;
  }

  @Override
  public void runVolts(double volts) {
    this.rollersVolts = volts;
  }

  @Override
  public void stop() {
    this.rollersVolts = 0.0;
  }
}
