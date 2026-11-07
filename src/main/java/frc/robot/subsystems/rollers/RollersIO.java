package frc.robot.subsystems.rollers;

import org.littletonrobotics.junction.AutoLog;

public interface RollersIO {

    @AutoLog
    public static class RollerIOInputs {
        double rollerVolts = 0.0;
        double statorCurrent = 0.0;
        double supplyCurrent = 0.0;
        double motorSpeed = 0.0;
    }

  public default void runVolts(double volts) {}
    public default void updateInputs(RollerIOInputs inputs) {}

  public default void stop() {}
}
