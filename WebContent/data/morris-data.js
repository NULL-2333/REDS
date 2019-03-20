$(function() {
	
	new Morris.Donut({
        element: 'wwwwww',//morris-donut-chart
        data: [{
            label: "Long sentence",
            value: 12
        }, {
            label: "Short sentence",
            value: 30
        }, {
            label: "Medium sentence",
            value: 20
        },{
        	label: "Medium",
            value: 20
        }],
        resize: true
    });
	
});
