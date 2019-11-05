function doCreate(f) {
	f.op.value="save";
	f.action="/fga/admin?service=adminControl&op=adminProperties&action=save&key="+ f.key.value+"&data="+ f.data.value;
	f.submit();
}

function doUpdate(f, i) {
	f.op.value="save";
	f.key.value=eval('f.key_'+i+'.value');
	f.data.value=eval('f.data_'+i+'.value');
	f.action="/fga/admin?service=adminControl&op=adminProperties&action=save&key="+ f.key.value+"&data="+ f.data.value;
	f.submit();
}

function doDelete(f, i) {
	f.op.value="delete";
	f.key.value=eval('f.key_'+i+'.value');
	f.data.value="";
	f.action="/fga/admin?service=adminControl&op=adminProperties&action=delete&key="+ f.key.value;
	f.submit();
}