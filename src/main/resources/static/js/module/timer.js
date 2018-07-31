/*计时器*/
Timer = function (time, selector, timeoutCallback) {

    let second = time * 60,
        qDom = $(selector),
        pid = 0,

    countdown = function () {
        let showTime = second--;

        if(showTime === 0) {
            timeoutCallback && timeoutCallback();
            stop();
        }

        let s = showTime % 60,
            m = Math.floor(showTime / 60) % 60,
            h = Math.floor(showTime / 3600);
        qDom.text([
            h,
            m < 10 ? '0' + m : m,
            s < 10 ? '0' + s : s
        ].join(":"));
    },

    start = function () {
       pid = setInterval(function () {
           countdown();
       }, 1000);
    },

    stop = function () {
        clearInterval(pid);
    };

    return {
        "start": start,
        "stop": stop
    }
};