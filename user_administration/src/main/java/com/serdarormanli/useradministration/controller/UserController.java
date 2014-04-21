package com.serdarormanli.useradministration.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.serdarormanli.useradministration.exception.UserFoundException;
import com.serdarormanli.useradministration.model.User;
import com.serdarormanli.useradministration.service.UserService;
import com.serdarormanli.useradministration.util.CaptchaGenerator;

/***
 * Main controller for this application
 * 
 * @author Serdar Ormanlı
 * 
 */
@Controller
public class UserController {
	private static final String HOME_VIEW = "home";

	@Autowired
	private UserService userService;

	@Autowired
	private CaptchaGenerator captchaGenerator;

	@ModelAttribute("user")
	public User prepareUserModel() {
		return new User();
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String showHome(Model model) {
		prepareUserModel();

		return HOME_VIEW;
	}

	/***
	 * Returns user list.
	 * 
	 * @return user list as json
	 */
	@RequestMapping(value = "/users/getlist", method = RequestMethod.POST)
	@ResponseBody
	public String showUserList() {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("Result", "OK");
		resultMap.put("Records", userService.getUsersList());
		String resultJson = null;

		try {
			resultJson = mapper.writeValueAsString(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultJson = "{\"Result\":\"ERROR\",\"Message\":\"" + e.getMessage() + "\"}";
		}

		return resultJson;
	}

	/***
	 * Gets name, surname, phonenumber and captcha as json. Does validation and
	 * inserts database.
	 * 
	 * @param requestJson
	 * @param session
	 * @return result json
	 */
	@RequestMapping(value = "/users/insert", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String insertUser(@RequestBody String requestJson, HttpSession session) {
		Map<String, String> map = new HashMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();
		String resultJson = null;

		try {
			map = mapper.readValue(requestJson, new TypeReference<HashMap<String, String>>() {
			});

			captchaGenerator.checkCaptcha((String) session.getAttribute("captchaId"), map.get("captchaValue"));

			if (StringUtils.isBlank(map.get("name")) || StringUtils.isBlank(map.get("surname"))) {
				throw new Exception("Name or surname is Empty");
			}

			User newUser = new User();
			newUser.setName(map.get("name"));
			newUser.setSurname(map.get("surname"));
			newUser.setPhoneNumber(map.get("phoneNumber"));

			userService.insertNewUser(newUser);

			resultJson = mapper.writeValueAsString(getResultMap());

		} catch (Exception e) {
			e.printStackTrace();
			resultJson = "{\"Result\":\"ERROR\",\"Message\":\"" + e.getMessage() + "\"}";
		} finally {
			session.removeAttribute("captchaId");
		}

		return resultJson;
	}

	@RequestMapping(value = "/users/insert", method = RequestMethod.GET)
	public String createHome() {
		prepareUserModel();

		return HOME_VIEW;
	}

	/***
	 * User update operation
	 * 
	 * @param updatedUser
	 * @return result json
	 */
	@RequestMapping(value = "/users/update", method = RequestMethod.POST)
	@ResponseBody
	public String updateUser(User updatedUser) {
		ObjectMapper mapper = new ObjectMapper();
		String resultJson = null;

		try {
			userService.updateUser(updatedUser);
			resultJson = mapper.writeValueAsString(getResultMap());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson = "{\"Result\":\"ERROR\",\"Message\":\"" + e.getMessage() + "\"}";
		}

		return resultJson;
	}

	/***
	 * User delete operation
	 * 
	 * @param user
	 *            id
	 * @return result json
	 */
	@RequestMapping(value = "/users/delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteUser(@RequestParam("id") String id) {
		ObjectMapper mapper = new ObjectMapper();
		String resultJson = null;

		try {
			userService.deleteUser(id);
			resultJson = mapper.writeValueAsString(getResultMap());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson = "{\"Result\":\"ERROR\",\"Message\":\"" + e.getMessage() + "\"}";
		}

		return resultJson;
	}

	/***
	 * Generates a captcha and sets id to session.
	 * 
	 * @param session
	 * @return base64 encoded value of captcha
	 */
	@RequestMapping(value = "/captcha", method = RequestMethod.GET)
	@ResponseBody
	public String getCaptcha(HttpSession session) {
		HashMap<String, String> result;

		try {
			result = captchaGenerator.getCaptcha();
			session.setAttribute("captchaId", result.get("ID"));
			return result.get("VALUE");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@ExceptionHandler({ UserFoundException.class })
	public ModelAndView handleDatabaseError(UserFoundException e) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home");
		modelAndView.addObject("errorMessage", "error.user.exist");
		modelAndView.addObject("user", prepareUserModel());

		return modelAndView;
	}

	/***
	 * Generates a result map with ok status
	 * 
	 * @return map with OK result
	 */
	private Map<String, String> getResultMap() {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("Result", "OK");
		return resultMap;
	}
}