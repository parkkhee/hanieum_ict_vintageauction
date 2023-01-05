import { React, useState} from "react";
import axios from 'axios';
import { useNavigate } from "react-router-dom";

const SignInForm =({ refreshMember }) => {
    const navigate = useNavigate();
    const [id, setId] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    //const [memberObj, setMemberObj] = useState(null);
    
    
    const onChange = (event) => {
        const {target: {name, value}} = event;
        if(name === "id"){
            setId(value)
        }else if(name === "password"){
            setPassword(value)
        }
    }

    const onSubmit = async (event) => {
        // 회원가입 or 로그인 버튼 ( newAccount에 따라 )
        event.preventDefault();
        // 입력받은 데이터를 객체에 담아
        // 로그인 api에 post 요청
        axios.post('/api/members/login', {
            id: id,
            password: password
        })
        .then(response => {
            console.log("로그인 성공")
            // 로그인 성공 시
            // App.js의 유저 정보 갱신 함수 호출
            // 전달받은 유저 정보를 갱신하여 홈으로 이동
            console.log(response.data);
            refreshMember(response.data)
            navigate("/")
            })
        .catch(error => alert(error.response.data))
    };
    const goToSignUp = () => {
        navigate("/sign-up")
    }
    return (
        <>
        <div className="signIn-form">
            <form onSubmit={onSubmit}>
                <label htmlFor="id-input">아이디</label>
                <input 
                name="id"
                placeholder="id" 
                required 
                value={id}
                onChange={onChange}
                className="id-input"
                id="id-input"/>
                <br/>
                <label htmlFor="password-input">비밀번호</label>
                <input 
                name="password"
                type="password" 
                placeholder="Password" 
                required 
                value={password}
                onChange={onChange}
                className="password-input"
                id="password-input"/>
                <br/>
                <button onClick={goToSignUp}
                className="signUp-submit">
                Sign Up
                </button>
                <input type="submit" 
                className="signIn-submit submit"
                value="Sign In"/>
                <br/>
                <span>아이디를 잊으셨습니까?</span>
                <br/>
                <span>비밀번호를 잊으셨습니까?</span>
                {error && <span className="authError">{error}</span>}
            </form>
            </div>
        </>
    )
}
export default SignInForm;