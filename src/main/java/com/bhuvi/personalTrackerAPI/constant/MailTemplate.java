package com.bhuvi.personalTrackerAPI.constant;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MailTemplate {

    public static String signupVerificationTemplate = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: #f4f7f9; }
                    .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
                    .header { background-color: #2d3436; padding: 30px; text-align: center; }
                    .header h1 { color: #00d2ff; margin: 0; font-size: 24px; letter-spacing: 1px; }
                    .content { padding: 40px; text-align: center; color: #2d3436; }
                    .otp-card { background-color: #f8f9fa; border: 2px dashed #cbd5e0; border-radius: 8px; padding: 20px; margin: 25px 0; }
                    .otp-code { font-size: 32px; font-weight: bold; letter-spacing: 8px; color: #0984e3; margin: 0; }
                    .footer { background-color: #f8f9fa; padding: 20px; text-align: center; font-size: 12px; color: #636e72; }
                    .btn { display: inline-block; padding: 12px 24px; background-color: #0984e3; color: white; text-decoration: none; border-radius: 6px; font-weight: 600; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>PERSONAL TRACKER</h1>
                    </div>
                    <div class="content">
                        <h2 style="margin-top: 0;">Verify Your Identity</h2>
                        <p>To keep your habits and goals secure, please use the following One-Time Password (OTP) to verify your account.</p>
            
                        <div class="otp-card">
                            <p style="margin-bottom: 10px; color: #636e72; font-size: 14px;">YOUR CODE</p>
                            <div class="otp-code">%s</div>
                        </div>
            
                        <p style="font-size: 14px; color: #b2bec3;">This code is valid for <b>5 minutes</b>. If you didn't request this, please ignore this email.</p>
                    </div>
                    <div class="footer">
                        &copy; 2026 Personal Tracker App. Built for progress.<br>
                        Sent via Render Cloud Services.
                    </div>
                </div>
            </body>
            </html>
            """;

}
