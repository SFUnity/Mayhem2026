package frc.robot.subsystems.pivot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

public class PivotIOTalonFX implements PivotIO {
    private final TalonFX pivot;
    private final TalonFXConfiguration config;

    public PivotIOTalonFX() {
        pivot = new TalonFX(PivotConstants.motorID);
        config = new TalonFXConfiguration();
        config.CurrentLimits.SupplyCurrentLimit = PivotConstants.supplyCurrentLimit;
        config.CurrentLimits.SupplyCurrentLimitEnable = true;
        pivot.getConfigurator().apply(config);
    }
}
