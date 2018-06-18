const apiRequest = function (url, data, method) {
    let isOk = false;
    return fetch(url, {
        method: method || 'GET',
        mode: 'cors',
        headers: {
            'credentials': 'include',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(function (response) {
        isOk = response.ok;
        return response.json();
    }).then(function (data) {
        if (isOk) {
            return data;
        }
        alert.error(data.msg);
        return Promise.reject(data.msg);
    });
};

const testPaper = (function ($) {

    let paper,
        stat = {
            current : 0,    // 当前题号
            score : 0,      // 总分
            selected : [],  // 已选情况
        },
        question = $("#questionContent");

    let init = function (subjectId, paperId) {
        apiRequest("/paper/start",
            {
                subjectId: subjectId,
                paperId: paperId
            },
            "POST")
            .then(function (data) {
                paper = data;
                // 开始计时
                countdown(data.testTime)
            })
            .then(function () {

            });
        },

        countdown = function (time) {

        },

        // 选择答案
        chooseAnswer = function (answer) {
            stat.selected[stat.current] = answer;
        },

        selectQuestion = function (qid) {
            stat.current = qid;
            question.html(template("questionTpl", {
                question: paper.questions[qid],
                current: qid
            }));
        }
    ;

    return {
        "init": init
    }

})(jQuery);

const alert = (function ($) {

    let showMessage = function (data) {

            let alert = $(template('alertTpl', data));

            alert.insertAfter('.navbar');

            setTimeout(function () {
                alert.alert('close');
            }, 2000)

        },
        error = function (message) {
            showMessage({message: message, type: 'danger'});
        },

        warning = function (message) {
            showMessage({message: message, type: 'warning'});
        },

        success = function (message) {
            showMessage({message: message, type: 'success'});
        },

        info = function (message) {
            showMessage({message: message, type: 'info'});
        };

    return {
        success: success,
        error: error,
        warning: warning,
        info: info
    }

})(jQuery);