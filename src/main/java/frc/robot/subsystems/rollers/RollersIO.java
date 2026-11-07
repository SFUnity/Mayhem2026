package frc.robot.subsystems.rollers;

import org.littletonrobotics.junction.AutoLog;

public interface RollersIO {

  @AutoLog
  public static class RollerIOInputs {
    double rollerVolts = 0.0;
  }

  public void updateInputs(RollerIOInputs inputs);

  public default void runVolts(double volts) {}

  public default void stop() {}
}
