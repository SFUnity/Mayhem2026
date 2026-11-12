package frc.robot.commands;

import static edu.wpi.first.wpilibj2.command.Commands.runOnce;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.rollers.Rollers;
import frc.robot.subsystems.rollers.Rollers.RollersState;

public class RobotCommands {
  public static Command Intake(Rollers rollers) {
    return runOnce(() -> rollers.setState(RollersState.INTAKE), rollers);
  }

  public static Command StopIntake(Rollers rollers) {
    return runOnce(() -> rollers.setState(RollersState.STOP), rollers);
  }

  public static Command Outtake(Rollers rollers) {
    return runOnce(() -> rollers.setState(RollersState.OUTTAKE), rollers);
  }
}
