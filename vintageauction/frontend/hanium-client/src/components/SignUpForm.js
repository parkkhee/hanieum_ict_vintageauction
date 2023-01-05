import { React, useState} from "react";
import axios from 'axios';
import { useNavigate } from "react-router-dom";

const AuthForm =({refreshMember}) => {
    const navigate = useNavigate();
    const [id, setId] = useState("");
    const [name, setName] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    // input 칸에 데이터가 바뀔때마다 리렌더링
    const onChange = (event) => {
        const {target: {name, value}} = event;
        if(name === "id"){
            setId(value)
        }else if(name == "name"){
            setName(value)
        }else if(name === "password"){
            setPassword(value)
        }
    }

    const signIn = async () => {
        // 로그인 api에 post 요청
        axios.post('/api/members/login', {
            id: id,
            password: password
        })
        .then(response => {
            // 로그인 성공 시
            // App.js의 유저 정보 갱신 함수 호출
            // 전달받은 유저 정보를 갱신하여 홈으로 이동
            console.log("로그인 성공")
            refreshMember(response.data)
            navigate("/")
            })
        .catch(error => alert(error.response.data))
    }

    const onSubmit = async (event) => {
        // 회원가입 버튼
        event.preventDefault();
        // 입력받은 데이터를 객체에 담아
        // 회원가입 api에 post 요청
        await axios.post('/api/members/new/', {
            id: id,
            name: name,
            password: password
        })
        .then(response => {
            console.log(response.data);
            // 회원가입 성공 시 해당 정보를 통해 로그인 요청
            signIn();
        })
        .catch(error => console.log(error.response.data))
    };
    const goToSignIn = () => {
        navigate("/sign-in")
    }
    return (
        <><div className="signUp-form">
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
                <label htmpFor="name-input">이름</label>
                <input 
                name="name"
                placeholder="name" 
                required 
                value={name}
                onChange={onChange}
                className="name-input"
                id="name-input"/>
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
                <button onClick={goToSignIn}
                className="signIn-submit">
                Sign In
                </button>
                <input type="submit" 
                className="signUp-submit submit"
                value="Sign Up"/>
                {error && <span className="authError">{error}</span>}
            </form>
            </div>
        </>
    )
}
export default AuthForm;