package com.ql.ecommerce_backend.services;

import com.ql.ecommerce_backend.enums.OtpPurpose;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendOtpEmail(String to, String otp, OtpPurpose purpose) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String subject = getSubjectBasedOnPurpose(purpose);
            String content = createOtpEmailContent(otp, purpose);

            helper.setText(content, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("no-reply@yourecommerce.com");

            mailSender.send(mimeMessage);
            log.info("OTP email sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send OTP email", e);
        }
    }

    private String getSubjectBasedOnPurpose(OtpPurpose purpose) {
        return switch (purpose) {
            case SIGNUP -> "Verify Your Email - OTP";
            case LOGIN -> "Login Verification - OTP";
            case PASSWORD_RESET -> "Password Reset Request - OTP";
            default -> "Your OTP Verification Code";
        };
    }

    private String createOtpEmailContent(String otp, OtpPurpose purpose) {
        String message = switch (purpose) {
            case SIGNUP -> "Thank you for signing up! Please use the following OTP to verify your account:";
            case LOGIN -> "Please use the following OTP to verify your login:";
            case PASSWORD_RESET -> "You requested to reset your password. Please use the following OTP to proceed:";
            default -> "Your verification code is:";
        };

        return """
               <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 5px;">
                   <h2 style="color: #333;">OTP Verification</h2>
                   <p>%s</p>
                   <div style="background-color: #f5f5f5; padding: 15px; border-radius: 5px; text-align: center; font-size: 24px; font-weight: bold; letter-spacing: 5px;">
                       %s
                   </div>
                   <p style="margin-top: 20px;">This OTP will expire in 10 minutes. Please do not share this OTP with anyone.</p>
                   <p>If you did not request this, please ignore this email.</p>
                   <hr style="border: none; border-top: 1px solid #e0e0e0; margin: 20px 0;">
                   <p style="color: #777; font-size: 12px;">This is an automated email. Please do not reply.</p>
               </div>
               """.formatted(message, otp);
    }

    @Async
    public void sendPasswordResetLink(String to, String resetLink) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String content = """
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 5px;">
                        <h2 style="color: #333;">Reset Your Password</h2>
                        <p>You requested to reset your password. Click the button below to create a new password:</p>
                        <a href="%s" style="display: inline-block; background-color: #4CAF50; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px; margin: 20px 0;">Reset Password</a>
                        <p>If the button doesn't work, you can copy and paste the following link into your browser:</p>
                        <p style="word-break: break-all;">%s</p>
                        <p>This link will expire in 30 minutes.</p>
                        <p>If you did not request a password reset, please ignore this email.</p>
                        <hr style="border: none; border-top: 1px solid #e0e0e0; margin: 20px 0;">
                        <p style="color: #777; font-size: 12px;">This is an automated email. Please do not reply.</p>
                    </div>
                    """.formatted(resetLink, resetLink);

            helper.setText(content, true);
            helper.setTo(to);
            helper.setSubject("Reset Your Password");
            helper.setFrom("no-reply@yourecommerce.com");

            mailSender.send(mimeMessage);
            log.info("Password reset email sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send password reset email", e);
        }
    }
}
