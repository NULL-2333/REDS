 $(function() {
    new Morris.Bar({
        element: 'we',
        data: [{
            y: 'Accuracy',
            a: 1,
            b: 0.9
        }, {
            y: 'Recall',
            a: 0.6,
            b: 0.65
        }, {
            y: 'Percision',
            a: 0.5,
            b: 0.4
        }, {
            y: 'F-Score',
            a: 0.75,
            b: 0.65
        }, {
            y: 'MacroScore',
            a: 0.50,
            b: 0.40
        }, {
            y: 'MicroScore',
            a: 0.75,
            b: 0.65
        }],
        xkey: 'y',
        ykeys: ['a', 'b'],
        labels: ['Plan A', 'Plan B'],
        hideHover: 'auto',
        gridTextSize:11,
        resize: true
    });   
});
