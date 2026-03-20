const resultElement = document.getElementById("result");
function showBrowserInfo() {
    const userAgent = navigator.userAgent; // это строка, которую браузер предоставляет сайту. В ней содержится информация о браузере, версии, ОС и иногда устройстве.
    let browserName = "Неизвестно";
    let browserVersion = "Неизвестно";
    
    if (userAgent.includes("YaBrowser")) {
        browserName = "Яндекс Браузер";
        const match = userAgent.match(/YaBrowser\/([0-9.]+)/);
        browserVersion = match ? match[1] : "Неизвестно";
    } else if (userAgent.includes("Chrome") && !userAgent.includes("Edg")) {
        browserName = "Google Chrome";
        const match = userAgent.match(/Chrome\/([0-9.]+)/);
        browserVersion = match ? match[1] : "Неизвестно";
    } else if (userAgent.includes("Firefox")) {
        browserName = "Mozilla Firefox";
        const match = userAgent.match(/Firefox\/([0-9.]+)/);
        browserVersion = match ? match[1] : "Неизвестно";
    } else if (userAgent.includes("Safari") && !userAgent.includes("Chrome")) {
        browserName = "Apple Safari";
        const match = userAgent.match(/Version\/([0-9.]+)/);
        browserVersion = match ? match[1] : "Неизвестно";
    } else if (userAgent.includes("Edg")) {
        browserName = "Microsoft Edge";
        const match = userAgent.match(/Edg\/([0-9.]+)/);
        browserVersion = match ? match[1] : "Неизвестно";
    }

    const browserInfo = 
`НАЗВАНИЕ БРАУЗЕРА: ${browserName}
ВЕРСИЯ БРАУЗЕРА: ${browserVersion}
USER AGENT: ${navigator.userAgent}
ПЛАТФОРМА: ${navigator.platform}
ЯЗЫК: ${navigator.language}
ONLINE: ${navigator.onLine ? 'Да' : 'Нет'}
`;
    resultElement.textContent = browserInfo;
}

function showOSInfo() {
    const userAgent = navigator.userAgent;
    let os = "Неизвестная ОС";
    
    if (userAgent.indexOf("Win") !== -1) os = "Windows";
    if (userAgent.indexOf("Mac") !== -1) os = "MacOS";
    if (userAgent.indexOf("Linux") !== -1) os = "Linux";
    if (userAgent.indexOf("Android") !== -1) os = "Android";
    if (userAgent.indexOf("like Mac") !== -1) os = "iOS";
    
    resultElement.textContent = `ОПЕРАЦИОННАЯ СИСТЕМА: ${os}`;
}

function showScreenSize() {
    const screenInfo = 
`ШИРИНА ЭКРАНА: ${screen.width} пикселей
ВЫСОТА ЭКРАНА: ${screen.height} пикселей
`;
    resultElement.textContent = screenInfo;
}

function showAvailableScreenSize() {
    const availableScreenInfo = 
`ДОСТУПНАЯ ШИРИНА ЭКРАНА: ${screen.availWidth} пикселей
ДОСТУПНАЯ ВЫСОТА ЭКРАНА: ${screen.availHeight} пикселей
`;
    resultElement.textContent = availableScreenInfo;
}

function showColorDepth() {
    resultElement.textContent = `ГЛУБИНА ЦВЕТА: ${screen.colorDepth} бит\nГЛУБИНА ПИКСЕЛЯ: ${screen.pixelDepth} бит`;
}

function showConnectionInfo() {
    const connectionStatus = navigator.onLine ? 
        "СТАТУС ПОДКЛЮЧЕНИЯ: ОНЛАЙН" : 
        "СТАТУС ПОДКЛЮЧЕНИЯ: ОФФЛАЙН";
    
    // Дополнительная информация о соединении, если доступно
    let connectionDetails = "";
    if (navigator.connection) {
        connectionDetails = `
СКОРОСТЬ: ${navigator.connection.downlink || 'Неизвестно'} Мбит/с
`;
    }
    resultElement.textContent = connectionStatus + connectionDetails;
}

function showPreferredLanguage() {
    const languages = navigator.languages ? 
        `Все языки: ${navigator.languages.join(', ')}` : 
        '';
    
    resultElement.textContent = `ПРЕДПОЧТИТЕЛЬНЫЙ ЯЗЫК: ${navigator.language}\n${languages}`;
}

function showCurrentURL() {
    const urlInfo = 
`ПОЛНЫЙ URL: ${location.href}
ПУТЬ: ${location.pathname}
`;
    resultElement.textContent = urlInfo;
}

function showProtocol() {
    resultElement.textContent = `ПРОТОКОЛ: ${location.protocol}`;
}

function showHost() {
    let hostInfo;
    
    if (location.protocol === 'file:') {
        hostInfo = 
`ХОСТ: Локальный файл (file://)
ИМЯ ХОСТА: Локальный файл
ПОРТ: по умолчанию
`;
    } else {
        hostInfo = 
`ХОСТ: ${location.host}
ИМЯ ХОСТА: ${location.hostname}
ПОРТ: ${location.port || 'по умолчанию'}
`;
    }
    resultElement.textContent = hostInfo;
}
