package frc.robot.subsystems.pivot;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pivot extends SubsystemBase {
  private final PivotIO io;
  private final PivotIOInputsAutoLogged inputs = new PivotIOInputsAutoLogged();

  private PivotState state = PivotState.F_DOWN;

  public enum PivotState {
    F_DOWN(PivotConstants.F_DOWN_ANGLE),
    F_L1(PivotConstants.F_L1_ANGLE),
    F_L2(PivotConstants.F_L2_ANGLE),
    F_L3(PivotConstants.F_L3_ANGLE),
    B_L3(PivotConstants.B_L3_ANGLE),
    B_L2(PivotConstants.B_L2_ANGLE),
    B_L1(PivotConstants.B_L1_ANGLE);

    private final double angle;

    PivotState(double angle) {
      this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
  }

  public Pivot(PivotIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);

    io.setMotorAngle(state.getAngle());
  }

  public void setState(PivotState state) {
    this.state = state;
  }

  public PivotState getState() {
    return this.state;
  }
}