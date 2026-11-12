package frc.robot.subsystems.rollers;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Rollers extends SubsystemBase {

  private final RollersIO io;
  private RollersIOInputsAutoLogged inputs = new RollersIOInputsAutoLogged();
  private RollersState state = RollersState.STOP;

  public Rollers(RollersIO io) {
    this.io = io;
  }

  public enum RollersState {
    STOP,
    INTAKE,
    OUTTAKE
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Intake", inputs);
    Logger.recordOutput("Intake/State", this.state);
    switch (state) {
      case STOP:
        io.stop();
        break;
      case INTAKE:
        io.runVolts(RollersConstants.intakeVolts);

        if (inputs.statorCurrent > RollersConstants.intakeStatorCurrentThreshold) {
          this.state = RollersState.STOP;
        }
      case OUTTAKE:
        io.runVolts(-RollersConstants.intakeVolts);
        break;
    }
  }

  public void setState(RollersState state) {
    this.state = state;
  }

  public RollersState getState() {
    return state;
  }
}
