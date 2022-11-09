package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.TimeUnit;

@TeleOp
public class MecanumTeleOp extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");//motor one
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");//motor three
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");// motor zero
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");//motor two

        // Setting up the claw on the robot
        Servo clawLeft = hardwareMap.servo.get("servo0");
        Servo clawRight = hardwareMap.servo.get("servo1");
        clawLeft.scaleRange(0.22, 0.33);
        clawRight.scaleRange(0.22, 0.37);

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart(); //hit start + a

        if (isStopRequested()) return;
        double adj = 0;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;
            double ry = gamepad1.right_stick_y;

            double lt = gamepad1.left_trigger;
            boolean open_claw = gamepad1.dpad_up;
            boolean close_claw = gamepad1.dpad_down;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);

            // We want to figure out what positions to set on the claw for open and close operations
            if (open_claw) {
                clawLeft.setPosition(1.0);
                clawRight.setPosition(1.0);
            } else if (close_claw) {
                clawLeft.setPosition(0.0);
                clawRight.setPosition(0.0);
            }
        }
    }
}
