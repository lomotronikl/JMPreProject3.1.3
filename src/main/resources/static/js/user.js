/*

alert( "Привет 1" );
*/
let tableLoaded = 0;



function activeateAdminPage() {
    $('#liAdmin').show();
    $('#spanTab').text("New user");
    $('#spanNewUser').text("Add new user");
    $('#buttonAdd').show();
    $('#myTab a[href="#admin"]').tab('show');
}
function activateUserInfo() {
    $('#liAdmin').hide();
    $('#spanTab').text("Profile");
    $('#spanNewUser').text("Profile");
    $('#buttonAdd').hide();

    fillUserInfo();


    $('#myTab a[href="#newUser"]').tab('show');

}
function fillUserInfo() {
    const myRequest = new Request("/admin/userinfo");
    fetch(myRequest)
        .then(response => response.json())
        .then(data => {
            $('#_id').val(data.id);
            $('#_name').val(data.name);
            $('#_lastName').val(data.lastName);
            $('#_username').val(data.username);

            const select = document.querySelector('#_userRoles').getElementsByTagName('option');
            for (let i = 0; i < select.length; i++) {
                if (select[i].value == 'ADMIN') select[i].selected = data.isAdmin;
                if (select[i].value == 'USER') select[i].selected = data.isUser;
            }

        });

}
async function buttonCreateEvent() {
    // Create tab
    var x = document.getElementById("createForm");
    const select = document.querySelector('#_userRoles').getElementsByTagName('option');
    let isAdmin = false;
    let isUser = false;
    for (let i = 0; i < select.length; i++) {
        if (select[i].value == 'ADMIN') isAdmin = select[i].selected;
        if (select[i].value == 'USER') isUser = select[i].selected;
    }
    const response = await fetch("/admin/create", {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({

            name: x.elements["_name"].value,
            lastName: x.elements["_lastName"].value,
            username: x.elements["_username"].value,
            password: x.elements["_password"].value,
            isAdmin: isAdmin,
            isUser: isUser
        })
    });

    response.json().then(data => {
        console.log(data);
    });

    tableLoaded = 0;
    loadTable();
    $('#myTab a[href="#admin"]').tab('show');
}

function fillProfile(id) {
    const myRequest = new Request("/admin/getUser/" + id);
    fetch(myRequest)
        .then(response => response.json())
        .then(data => {
            $('.editForm #id').val(data.id);
            $('.editForm #name').val(data.name);
            $('.editForm #lastName').val(data.lastName);
            $('.editForm #username').val(data.username);

            const select = document.querySelector('#userRoles').getElementsByTagName('option');
            for (let i = 0; i < select.length; i++) {
                if (select[i].value == 'ADMIN') select[i].selected = data.isAdmin;
                if (select[i].value == 'USER') select[i].selected = data.isUser;
            }
        });
}

function buttonEditEvent(id) {
    fillProfile(id);
    $('.editForm #editModal').modal();
}
function  buttonDeleteConfirm() {
    const x = document.getElementById("formDelete");

    const myRequest = new Request("/admin/delete/"+ x.elements["id_"].value);
    fetch(myRequest , {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: x.elements["id_"].value
        })
    }).then(response => {
        $('.deleteForm #deleteModal').close;
        tableLoaded=0;
        loadTable();
    });


}

function buttonDeleteEvent(id) {
    ///delete dialog
    const myRequest = new Request("/admin/getUser/" + id);
    fetch(myRequest)
        .then(response => response.json())
        .then(data => {
            $('.deleteForm #id_').val(data.id);
            $('.deleteForm #name_').val(data.name);
            $('.deleteForm #lastName_').val(data.lastName);
            $('.deleteForm #username_').val(data.username);

            const select = document.querySelector('#userRoles_').getElementsByTagName('option');
            for (let i = 0; i < select.length; i++) {
                if (select[i].value == 'ADMIN') select[i].selected = data.isAdmin;
                if (select[i].value == 'USER') select[i].selected = data.isUser;
            }
        });
    $('.deleteForm #deleteModal').modal();
}

function funRoleAdmin() {
    var chbox;
    chbox = document.getElementById('roleAdmin');
    if (chbox.checked) {
        chbox.value = "true";
    } else {
        chbox.value = "false";
    }
}

function funRoleUser() {

    var chbox = document.getElementById('roleUser');
    if (chbox.checked) {
        chbox.value = "true";
    } else {
        chbox.value = "false";
    }
}

async function buttonSaveEvent() {
    //  update user (post)
    var x = document.getElementById("updateForm");
    const select = document.querySelector('#userRoles').getElementsByTagName('option');
    let isAdmin = false;
    let isUser = false;
    for (let i = 0; i < select.length; i++) {
        if (select[i].value == 'ADMIN') isAdmin = select[i].selected;
        if (select[i].value == 'USER') isUser = select[i].selected;
    }
    const response = await fetch("/admin/update", {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id : x.elements["id"].value,
            name: x.elements["name"].value,
            lastName: x.elements["lastName"].value,
            username: x.elements["username"].value,
            password: x.elements["password"].value,
            isAdmin: isAdmin,
            isUser:  isUser
        })
    });

    response.json().then(data => {
        console.log(data);
    });

    tableLoaded=0;
    loadTable();
    $('.editForm #editModal').close;
}

function loadTable() {
   // alert("!");
    if (tableLoaded == 0) {
        tableLoaded = 1;

        const myRequest = new Request("/admin/index");

        fetch(myRequest)
            .then(response => response.json())
            .then(response => {

                let rows = "";

                response.forEach(function (user, i, response) {
                    rows += '<tr>';
                    rows += '<td>' + user.id + '</td>';
                    rows += '<td>' + user.name + '</td>';
                    rows += '<td>' + user.lastName + '</td>';
                    rows += '<td>' + user.username + '</td>';
                    rows += '<td>' + user.isAdmin + '</td>';
                    rows += '<td>' + user.isUser + '</td>';
                    rows += '<td><a href="#" onclick="buttonEditEvent(' + user.id + ')" class="btn btn-primary"'
                        + ' role="button">Edit</a></td>';
                    rows += '<td><a href="#" onclick="buttonDeleteEvent(' + user.id + ')" class="btn btn-danger"'
                        + ' role="button">Delete</a></td>';
                    rows += '</tr>';
                });

                $('.table tbody').html(rows);

            });

    }
}

$(document).ready(function () {



    const myRequest = new Request("/admin/userinfo");
    fetch(myRequest)
        .then(response => response.json())
        .then(data => {
               if (data.isAdmin) {
                   loadTable();
               }
               else {
                   $("#buttonAdmin").hide();
                   activateUserInfo();

               }


        });// then fetch userinfo

    $('#myTab a[href="#newUser"]').click(function (link) {
        $('#_id').val("");
        $('#_name').val("");
        $('#_lastName').val("");
        $('#_username').val("");

        const select = document.querySelector('#_userRoles').getElementsByTagName('option');
        for (let i = 0; i < select.length; i++) {
            if (select[i].value == 'ADMIN') select[i].selected = false;
            if (select[i].value == 'USER') select[i].selected = false;
        }
    })


});


