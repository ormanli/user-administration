package com.serdarormanli.useradministration.util;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.github.cage.Cage;
import com.github.cage.YCage;
import com.serdarormanli.useradministration.exception.InvalidCaptchaException;
import com.serdarormanli.useradministration.model.Captcha;
import com.serdarormanli.useradministration.repository.CaptchaRepository;

/***
 * Singleton captcha generator
 * @author Serdar ORMANLI
 *
 */
@Service
@Scope("singleton")
public class CaptchaGenerator {

	@Autowired
	CaptchaRepository captchaRepository;

	/***
	 * Generates a captcha and saves to database
	 * 
	 * @return {"ID"=id of captcha,"VALUE"=base64 encoded image}
	 * @throws Exception
	 */
	public synchronized HashMap<String, String> getCaptcha() throws Exception {
		Cage cage = new YCage();

		String captchaValue = RandomStringUtils.randomAlphanumeric(8);
		String id = UUID.randomUUID().toString();
		byte[] c = cage.draw(captchaValue);

		HashMap<String, String> result = new HashMap<String, String>();
		result.put("ID", id);
		result.put("VALUE", new StringBuffer("data:image/gif;base64,").append(Base64.encodeBase64String(c)).toString());

		captchaRepository.save(new Captcha(id, captchaValue, Base64.encodeBase64String(c), new Date()));

		return result;
	}

	/***
	 * Checks value of given captcha, if wrong throws
	 * {@link InvalidCaptchaException}
	 * 
	 * @param id
	 *            of captcha
	 * @param value
	 *            to check
	 * @throws Exception
	 */

	public synchronized void checkCaptcha(String id, String value) throws Exception {
		boolean result = true;

		result = result && captchaRepository.exists(id);
		result = result && StringUtils.equals(captchaRepository.findOne(id).getValue(), value);

		captchaRepository.delete(id);

		if (!result) {
			throw new InvalidCaptchaException("You entered wrong value!");
		}
	}
}
