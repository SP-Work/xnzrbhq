    function toast(msg){
    Android.showToast(msg);
    }

    function loadALineChart(){
             // 必须加JOSN.parse 转换数据类型
             var option = JSON.parse(Android.setGsonOption());
             var chart2Doc = document.getElementById('main');
             var myChart2 = require('echarts').init(chart2Doc);

             myChart2.setOption(option);
             document.getElementById('textcontent').innerHTML=option;
             toast(option);
         }

    /**
    type :  1 - 饼状图
            2 - 柱状图
    */
    function loadAChart(type){
        // 必须用JSON.parse() 转换一下，才可以显示，否则数据类型会不对
        var option = JSON.parse(Android.getPieChartOptions(type));
        var chart2Doc = document.getElementById('main');
        var myChart2 = require('echarts').init(chart2Doc);

        myChart2.setOption(option);
        document.getElementById('textcontent').innerHTML=option;
    }