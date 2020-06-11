package net.oopscraft.application.user;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.IdGenerator;
import net.oopscraft.application.email.Email;
import net.oopscraft.application.email.EmailService;
import net.oopscraft.application.user.Verification.IssueType;

@Service
public class VerificationService {
	
	@Autowired
	VerificationRepository verificationRepository;
	
	@Autowired
	EmailService emailService;
	
	/**
	 * Issues verification data.
	 * @param issueType
	 * @param issueAddress
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Verification issueVerification(IssueType issueType, String issueAddress, String userId) throws Exception {
		Verification verification = new Verification();
		verification.setId(IdGenerator.uuid());
		verification.setIssueType(issueType);
		verification.setIssueAddress(issueAddress);
		verification.setUserId(userId);
		verification.setIssueDate(new Date());
		verification.setUse(false);
		
		// creates code
		Random random = new Random();
		int code = 100000 + random.nextInt(899999);
		verification.setCode(Integer.toString(code));
		verificationRepository.saveAndFlush(verification);
		
		// sends email
		if(issueType == IssueType.EMAIL) {
			Email email = new Email();
			email.setSubject("회원가입");
			email.setContent("인증코드: " + code);
			email.setReceiver(issueAddress);
			emailService.sendEmail(email);
		}else if(issueType == IssueType.SMS) {
			throw new Exception("SMS Verification not implemented.");
		}
		
		// returns
		return verification;
	}
	
	/**
	 * Returns weather verification code is correct.
	 * @param verification
	 * @return
	 * @throws Exception
	 */
	public boolean isCorrectCode(String id, String code) throws Exception {
		Verification one = verificationRepository.findOne(id);
		if(one.getCode().contentEquals(code)) {
			return true;
		}
		return false;
	}

}
