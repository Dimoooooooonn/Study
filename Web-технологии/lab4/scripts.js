function show_information()
{ alert("Филимонов Д.Е. Группа ИКПИ-32 Лабораторная №4 "); }

function show_information2(elem)
{ let x = parseInt(elem.value);
    if (!isNaN(x) && 3.00 <= x && x <= 5.00) {
        alert("Ваш средний балл – " + elem.value + " балла"); }
    else { alert("Нет данных!"); }}

function send(family, radio1, radio2, radio3)
{ let markProg = parseInt(radio1.value), markMath = parseInt(radio2.value); markPhyz =
    parseInt(radio3.value);
    document.write("<h2 style='font-size: 20px; color:green';>Фамилия: " + family.value + "</h2>");
    document.write("<h2 style='font-size: 24px; color:blue'>Оценка по дисциплине «Программирование»: " + markProg +
    "</h2>");
    document.write("<h2 style='font-size: 24px; color:blue'>Оценка по дисциплине «Математика»: " + markMath + "</h2>");
    document.write("<h2 style='font-size: 24px; color:blue'>Оценка по дисциплине «Физика»: " + markPhyz + "</h2>");
    document.write("<h2 style='font-size: 24px; color:blue'>Рейтинг: " + ((markProg + markMath + markPhyz) / 3) +"</h2>");
}

function checkEmail() {
    const email = document.getElementById("email").value;
    const message = document.getElementById("message");
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (emailRegex.test(email)) {
        message.style.color = "green";
        message.textContent = "Email введён правильно!";
        lastCheck = true;
    } else {
        message.style.color = "red";
        message.textContent = "Неверный формат email!";
        lastCheck = false;
    }

    return false; 
}
let col = 1;
container.ondblclick = function() {
    if (col == 1) {
        container.style.backgroundColor = "#5f905fff"; 
        col = 0;
    } else {
        container.style.backgroundColor = "#ffffffff"; 
        col = 1;
    }
};
