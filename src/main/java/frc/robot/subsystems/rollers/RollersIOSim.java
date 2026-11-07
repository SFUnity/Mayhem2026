package frc.robot.subsystems.rollers;

public class RollersIOSim implements RollersIO {
  private double rollersVolts = 0.0;
  private double statorCurrent = 0.0;
  private double supplyCurrent = 0.0;
  private double motorSpeed = 0.0;

  public RollersIOSim() {}

  @Override
  public void updateInputs(RollersIOInputs inputs) {
    inputs.rollersVolts = rollersVolts;
    inputs.motorSpeed = motorSpeed;
    inputs.statorCurrent = statorCurrent;
    inputs.supplyCurrent = supplyCurrent;
  }

  @Override
  public void runVolts(double volts) {
    rollersVolts = volts;
  }

  @Override
  public void stop() {
    rollersVolts = 0.0;
  }
}