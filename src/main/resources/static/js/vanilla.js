
document.addEventListener("DOMContentLoaded", loadChart);

// document.addEventListener("DOMContentLoaded", loadPersons);

function loadPersons() {
    let locationArray = getPath();
    let locationArgs = getArgs();
    let csrf = get_csrf();
    console.log(locationArray);
    console.log(locationArgs);
    // console.log(csrf);






    if(locationArray[locationArray.length - 2] === "person") {
        let personId = locationArray[locationArray.length - 1]

        let url = "/api/person/" + personId + "/brigade";
        if(locationArgs) {
            url += "?" + encodeURI(locationArgs);
        }

        let xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        // xhr.setRequestHeader("X-CSRF-Token", "csrf");
        xhr.onload = () => {
            console.log(JSON.parse(xhr.response));
            document.getElementById("card-body").innerHTML = "Бригада найдена";
        };
        xhr.send(null);
    }


}

function get_csrf() {
    return document.getElementsByName("_csrf")[0].value;
}

function getPath() {
    return window.location.href.split("?")[0].split("/").filter(item => item);
}

function getArgs() {
    locationArgs = window.location.href.split("?");
    if(locationArgs.length > 1) {
        return JSON.parse('{"' + decodeURI(locationArgs[1].replace(/&/g, "\",\"").replace(/=/g,"\":\"")) + '"}');
    }
    return null;
}

function loadChart() {

    const labels = [
        'January',
        'February',
        'March',
        'April',
        'May',
        'June',
    ];

    const data = {
        labels: labels,
        datasets: [{
            label: 'My First dataset',
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            data: [0, 10, 5, 2, 20, 30, 45],
        }]
    };

    const config = {
        type: 'line',
        data: data,
        options: {}
    };

    const myChart = new Chart(document.getElementById("chartStat"), config);
}


function deletePerson() {
    if(confirm("Вы уверены, что хотите удалить страницу?")) {
        let locationArray = window.location.href.split("/");
        let personId = locationArray[locationArray.length - 2]
        let url = "/person/" + personId + "/delete";
        /*let props = {"id": document.getElementsByName("id")[0].value}*/
        let csrf = document.getElementsByName("_csrf")[0].value;
        let xhr = new XMLHttpRequest();
        xhr.open('DELETE', url, true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhr.setRequestHeader("X-CSRF-Token", csrf);

        xhr.ontimeout  = () => alert("Время ожидания вышло :(");
        xhr.onload = () => window.location = "/logout";

        /*xhr.send(JSON.stringify(props));*/
        xhr.send(null);
    }
}

