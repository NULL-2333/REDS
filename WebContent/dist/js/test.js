<script>
var per=[
		{id:1,ProjectName:"A",Description:"aaaaa",date:"2018.1.1"},
		{id:2,ProjectName:"B",Description:"aaaa",date:"2018.1.2"},
		{id:3,ProjectName:"C",Description:"aaa",date:"2018.1.3"},
		{id:4,ProjectName:"D",Description:"aa",date:"2018.1.4"},
];
window.onload = function(){
	var tbody = document.getElementById("tbMain");
	for(var i=0;i<per.length;i++){
		var trow = getDataRow(per[i]);
		tbody.appendChild(trow);
	}
}

function getDataRow(h){
	//create row
	var row = document.createElement('tr');
	
	//create 1st col,id
	var idCell = document.createElement('td');
	//fill with data
	idCell.innerHTML = h.id;
	//add 
	row.appendChild(idCell);
	
	//create 2nd col,name
	var nameCell = document.createElement("td");
	nameCell.innerHTML = h.ProjectName;
	row.appendChild(nameCell);
	
	//create 3rd col, description
	var desCell = document.createElement("td");
	desCell.innerHTML = h.Description;
	row.appendChild(desCell);
	
	//create 4th col, date
	var dateCell = document.createElement)("td");
	dateCell.innerHTML = h.date;
	row.appendChild(dateCell);
	
	return row;
}
</script>