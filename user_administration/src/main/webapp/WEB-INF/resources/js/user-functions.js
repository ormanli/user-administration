$(document).ready(function() {
	$('#phoneNumber').mask('(000) 000-0000');

	var name = $("#name"),
		surname = $("#surname"),
		phoneNumber = $("#phoneNumber"),
		captchaValue = $("#captchaValue"),
		allFields = $([]).add(name).add(surname).add(phoneNumber).add(captchaValue),
		tips = $(".validateTips");

	function updateTips(t) {
		tips.text(t).addClass("ui-state-highlight");
		setTimeout(function() {
			tips.removeClass("ui-state-highlight", 1500);
		}, 500);
	}

	function checkLength(o, n, min, max) {
		if (o.val().length > max || o.val().length < min) {
			o.addClass("ui-state-error");
			updateTips("Length of " + n + " must be between " + min + " and " + max + ".");
			return false;
		} else {
			return true;
		}
	}

	function checkRegexp(o, regexp, n) {
		if (!(regexp.test(o.val()))) {
			o.addClass("ui-state-error");
			updateTips(n);
			return false;
		} else {
			return true;
		}
	}

	function getCaptcha() {
		$.get('/useradministration/spring/captcha', function(captchaImg) {
			$('#captcha').attr("src", captchaImg);
		});
	}

	$("#dialog-form").dialog({
		autoOpen: false,
		height: 300,
		width: 350,
		modal: true,
		buttons: {
			"Create an account": function() {
				var bValid = true;
				allFields.removeClass("ui-state-error");

				bValid = bValid && checkLength($("#name"), "name", 3, 16);
				bValid = bValid && checkLength($("#surname"), "surname", 3, 16);

				if (bValid) {

					var name = $('#name').val();
					var surname = $('#surname').val();
					var phoneNumber = $('#phoneNumber').val();
					var captchaValue = $('#captchaValue').val();
					var json = {
						"name": name,
						"surname": surname,
						"phoneNumber": phoneNumber,
						"captchaValue": captchaValue
					};
					var $this = $(this);
					$.ajax({
						url: "/useradministration/spring/users/insert.json",
						data: JSON.stringify(json),
						type: "POST",

						beforeSend: function(xhr) {
							xhr.setRequestHeader("Accept", "application/json");
							xhr.setRequestHeader("Content-Type", "application/json");
						},
						success: function(resultJson) {
							console.log(resultJson.Result);
							if ("OK" === resultJson.Result) {
								$('#myMessages').text('User created succesfully');
								$('#myMessages').show();
								$('#myMessages').fadeOut(10000);
								$('#UserTableContainer').jtable('load');
								$this.dialog("close");
							} else {
								$('#captchaValue').addClass("ui-state-error");
								updateTips(resultJson.Message);
								getCaptcha();
							}
						}
					});

				}
			},
			Cancel: function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			allFields.val("").removeClass("ui-state-error");
		}
	});

	$("#create-user").button().click(function() {
		getCaptcha();
		$("#dialog-form").dialog("open");
	});

	$("#captcha-button").button().click(function() {
		getCaptcha();
	});

	$('#UserTableContainer').jtable({
		title: 'User List',
		defaultSorting: 'Name ASC',
		actions: {
			listAction: '/useradministration/spring/users/getlist',
			deleteAction: '/useradministration/spring/users/delete',
			updateAction: '/useradministration/spring/users/update'
		},
		fields: {
			id: {
				key: true,
				create: false,
				edit: false,
				list: false
			},
			name: {
				title: 'Name',
				width: '23%'
			},
			surname: {
				title: 'Surname',
				width: '23%'
			},
			phoneNumber: {
				title: 'Phone Number',
				inputClass: 'edit-phoneNumber',
				width: '23%'
			},

		},
		formCreated: function(event, data) {
			data.form.find('[name=phoneNumber]').mask('(000) 000-0000');
			data.form.find('input[name="name"]').addClass('validate[required]');
			data.form.find('input[name="surname"]').addClass('validate[required]');
			data.form.validationEngine();
		},
		formSubmitting: function(event, data) {
			return data.form.validationEngine('validate');
		},
		formClosed: function(event, data) {
			data.form.validationEngine('hide');
			data.form.validationEngine('detach');
		}
	});

	$('#UserTableContainer').jtable('load');

});
