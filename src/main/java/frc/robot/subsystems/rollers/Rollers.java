package frc.robot.subsystems.rollers;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

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
    switch (state) {
      case STOP:
        io.stop();
        break;
      case INTAKE:
        io.runVolts(5); //magic numbers
        break;
      case OUTTAKE:
        io.runVolts(-5); //magic numbers
        break;
    }
  }

  public void setState(RollersState state) {
    this.state = state;
  }
}
