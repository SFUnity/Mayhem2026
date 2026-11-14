package frc.robot.subsystems.pivot;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pivot extends SubsystemBase {
  private final PivotIO io;
  private final PivotIOInputsAutoLogged inputs = new PivotIOInputsAutoLogged();

  public enum PivotState {
    F_DOWN,
    F_L1,
    F_L2,
    F_L3,
    B_L3,
    B_L2,
    B_L1,
    B_DOWN,
  }

  public Pivot(PivotIO io) {
    this.io = io;
  }
}
