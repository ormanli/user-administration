function retrieveGuests() {
	var url = '/useradministration/spring/guests';
	
	if ($('#searchSurname').val() != '') {
		url = url + '/' + $('#searchSurname').val();
	}
	
	$("#resultsBlock").load(url);
}