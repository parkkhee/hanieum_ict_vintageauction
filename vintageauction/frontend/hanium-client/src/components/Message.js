import React from "react"
import dayjs from 'dayjs'
import isLeapYear from 'dayjs/plugin/isLeapYear'
import 'dayjs/locale/ko'

const Message =(messageObj) => {
    const m = messageObj.messageObj
    const date = dayjs(m.sendDateTime).format('hh:mm')
    dayjs.extend(isLeapYear);
    dayjs.locale('ko');
    return (
        <div className="message-container">
            <div className="message-content">{m.content}</div>
            <div className="message-time">{date}</div>
        </div>
    )

}

export default Message