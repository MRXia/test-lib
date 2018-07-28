const questionType = (function ($) {

    let question = $("#questionContent"),
        answerArray = ['A', 'B', 'C', 'D'],
        checked = $('<span class="oi oi-check"></span>');
    /*
     * 问题类型公共类
     */
    class QuestionType {

        constructor(tplId, question) {
            this.tplId = tplId;
            this.selected = '';
            this.rightAnswer = '';
            this.score = 0;
            this.tip = '';
            $.extend(this, question);
        }

        // 构建题目数据
        createData() {
            return this;
        }

        // 使用题目数据和题号，调用对应模板，构建题目
        createQuestion(index, questionSize) {
            return template(this.tplId, {
                question: this.createData(),
                current: index,
                isLast: index === questionSize - 1
            });
        }

        // 选择答案操作
        chooseAnswer(answer, item) {
            this.checkedAnswer(answer, item);
            this.checkedOption(answer, item);
        }

        // 选择答案赋值操作
        checkedAnswer(answer) {
            this.selected = answer;
        }

        // 选择答案视图操作
        checkedOption(answer) {
        }

        // 展示提示与正确答案
        showRightAnswer() {
        }

        /* 多选题选择答案时，根据答案列表与当前选项，返回选择后的结果
         * 遍历A,B,C,D选项，如果当前答案包含而被选中，则去除选项，如果当前答案未包含而选中，则添加该选项
         * 如已选择"AC", 若选择"B"选项，则返回"ABC";若选择"A"选项，则返回"C"
         */
        static checkedMultiAnswer(answer, currentAnswer) {
            return answerArray.map(value => {
                let choose = value === answer;
                if (currentAnswer.indexOf(value) === -1) {
                    return choose ? value : '';
                } else {
                    return choose ? '' : value;
                }
            }).join('');
        }

        calScore() {
            return this.selected === this.rightAnswer ? this.score : 0;
        }
    }

    /*单选题类型*/
    class SingleChoiceQuestion extends QuestionType {
        constructor(question) {
            super("singleChoiceTpl", question);
        }

        checkedOption(answer) {
            $("#answer" + answer).append(checked);
        }

        showRightAnswer() {
            $("#answer" + this.rightAnswer).addClass("bg-success");
            if (this.selected && this.selected !== this.rightAnswer) {
                $("#answer" + this.selected).addClass("bg-danger");
            }
        }
    }

    /*多选题类型*/
    class MultipleChoiceQuestion extends QuestionType {
        constructor(question) {
            super("multipleChoiceTpl", question);
        }

        checkedAnswer(answer) {
            this.selected = QuestionType.checkedMultiAnswer(answer, this.selected);
        }

        checkedOption() {

            let answer = this.selected;
            // 多选题添加选中样式时需使用checked的clone对象
            question.find(".list-group .list-group-item").children().remove();
            answer.split('').forEach(value => {
                $("#answer" + value).append(checked.clone());
            });
        }

        showRightAnswer() {
            this.rightAnswer.split('').forEach(val => {
                $("#answer" + val).addClass("bg-success");
            });

            if (this.selected) {
                this.selected.split('').forEach(val => {
                    if (this.rightAnswer.indexOf(val) === -1) {
                        $("#answer" + val).addClass("bg-danger");
                    }
                });
            }
        }
    }

    /*判断题类型*/
    class TrueOrFalseQuestion extends QuestionType {
        constructor(question) {
            super("trueFalseTpl", question);
        }

        static getAnswerId(answer) {
            return "#answer" + (answer === '正确' ? 'True' : 'False');
        }

        checkedOption(answer) {
            $(TrueOrFalseQuestion.getAnswerId(answer)).append(checked);
        }

        // 判断题做对得分，做错扣分, 不做0分
        calScore() {
            if (this.selected) {
                return this.selected === this.rightAnswer ? this.score : -this.points;
            } else {
                return 0;
            }
        }

        showRightAnswer() {
            $(TrueOrFalseQuestion.getAnswerId(this.rightAnswer)).addClass("bg-success");
            if (this.selected && this.selected !== this.rightAnswer) {
                $(TrueOrFalseQuestion.getAnswerId(this.selected)).addClass("bg-danger");
            }
        }
    }

    /*操作题类型*/
    class OperateQuestion extends QuestionType {
        constructor(question) {
            super("operateTpl", question);
            this.selected = [];
            this.rightAnswer = this.rightAnswer.split('__#__');
        }

        createData() {

            // 如果操作题未被渲染，则进行预处理
            if (!this.items) {
                this.renderItems();
            }

            return this;
        }

        renderItems() {

            let title = "";
            let items = [];
            let item;

            // 通过<br>切割操作题问题，进行预处理
            let itemStart = false;
            let itemStatus;

            this.title.split("<br>").forEach(value => {

                // 排除字符串为空的行
                if (!$.trim(value)) {
                    return;
                }

                // 判断小题是否开始, 如果未开始，则为大题目，拼接进title
                itemStart = itemStart || /第1小题\./.test(value);
                if (!itemStart) {
                    title += value + "<br>";
                    return;
                }

                // 处理单个选项和题目不止一行的情况，通过起始字符变换状态
                if (/第\d小题\./.test(value)) {
                    if (item) {
                        items.push(item);
                    }
                    item = {title: ''};
                    itemStatus = 'title';
                } else {
                    let letter = $.trim(value).substr(0, 1);
                    if ("A_B_C_D".indexOf(letter) !== -1) {
                        let key = "answer" + letter;
                        item[key] = value;
                        itemStatus = 'key';
                    } else {
                        item[itemStatus] += value;
                    }
                }
            });

            // 放入最后一题
            if (item) {
                items.push(item);
            }

            this.title = title;
            this.items = items;
        }

        checkedAnswer(answer, item) {
            let currentAnswer = this.selected[item] || '';
            this.selected[item] = QuestionType.checkedMultiAnswer(answer, currentAnswer);
        }

        checkedOption(answer, item) {
            if (typeof(answer) === "string" && typeof(item) !== "undefined") {
                this.checkedItem(item);
            } else {
                answer.forEach((value, index) => {
                    this.checkedItem(index);
                })
            }
        }

        checkedItem(item) {
            question.find(".list-group#item" + item + " .list-group-item").children().remove();
            this.selected[item].split('').forEach(value => {
                $("#item" + item + "-answer" + value).append(checked.clone());
            });
        }

        // 操作题平均每小题2分，答对全部选项得2分，未答对全部得1分，答错选项则为0分
        calScore() {
            let rightAnswer = this.rightAnswer;
            let eachScore = this.score / (this.items ? this.items.length : 5);
            let totalScore = 0;

            this.selected.forEach((selected, index) => {
                let answer = rightAnswer[index];
                if (selected === answer) {
                    totalScore += eachScore;
                } else {
                    let split = selected.split('');
                    for (let i = 0; i < split.length; i++) {
                        if (answer.indexOf(split[i]) === -1) {
                            return;
                        }
                    }
                    totalScore += eachScore / 2;
                }
            });

            return totalScore;
        }

        showRightAnswer() {
            this.rightAnswer.forEach((answer, item) => {
                answer.split('').forEach(val => {
                    $("#item" + item + "-answer" + val).addClass("bg-success");
                });

                if (this.selected[item]) {
                    this.selected[item].split('').forEach(val => {
                        if (answer.indexOf(val) === -1) {
                            $("#item" + item + "-answer" + val).addClass("bg-danger");
                        }
                    });
                }
            });
        }
    }

    return {
        "SingleChoiceQuestion": SingleChoiceQuestion,
        "MultipleChoiceQuestion": MultipleChoiceQuestion,
        "TrueOrFalseQuestion": TrueOrFalseQuestion,
        "OperateQuestion": OperateQuestion
    }
})(jQuery);
