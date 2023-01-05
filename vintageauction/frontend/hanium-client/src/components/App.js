import React, {useEffect, useState} from 'react';
import AppRouter from './AppRouter';
import "../css/App.css"
import Footer from './Footer';
function App() {
    const [memberObj ,setMemberObj] = useState(null);
    // 현재 로컬스토리지에 저장되어있는 유저 정보 체크
    // 정보 존재할 시 자동으로 클라이언트 실행 시 로그인 처리
    // 세션토큰 존재 여부에 따른 로그인 처리로 수정 필요
    const getLoggedInfo = () => {
        if(localStorage.getItem("memberObj") != null){
            console.log(JSON.parse(localStorage.getItem("memberObj")).memberName)
            const LoggedInObj = JSON.parse(localStorage.getItem("memberObj"))

            setMemberObj({
                memberId: LoggedInObj.memberId,
                memberName: LoggedInObj.memberName,
                memberPassword: LoggedInObj.memberPassword
            })
        }
    }
    // 렌더링 시 getLoggedInfo 함수 한번만 실행
    useEffect(getLoggedInfo, []);
    const refreshMember = (memberObj) => {
        // 유저 정보 변경(회원가입 or 로그인) 시 
        // 재 랜더링 하는 함수
        if (memberObj){
            // 로그인 시 로컬스토리지에 유저정보 저장
            setMemberObj(memberObj);
            localStorage.setItem('memberObj', JSON.stringify(memberObj))
        }else{
            // 로그아웃 시 로컬스토리지에서 유저정보 삭제
            setMemberObj(null)
            localStorage.removeItem('memberObj')
        }
    }
    return (
        <div className='app-container'>
            {/* 앱 라우터에 유저 정보 및 유저 정보 갱신 함수 전달 */}
            <AppRouter memberObj={memberObj} refreshMember={refreshMember}/>
            <Footer />
        </div>
    );
}

export default App;