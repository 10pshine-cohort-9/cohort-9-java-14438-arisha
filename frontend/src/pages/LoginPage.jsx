import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function LoginPage() {
    const [identifier, setIdentifier] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const navigate = useNavigate();

    async function handleSubmit(event) {
        event.preventDefault();
        setError("");

        try {
            const response = await fetch("/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    identifier: identifier,
                    password: password,
                }),
            });

            if (!response.ok) {
                setError("Invalid email, phone number, or password");
                return;
            }

            const data = await response.json();

            localStorage.setItem("token", data.token);

            navigate("/contacts");
        } catch {
            setError("Unable to connect to the server");
        }
    }

    return (
        <div>
            <h1>Login</h1>

            <form onSubmit={handleSubmit}>
                <div>
                    <label>Email or Phone Number</label>
                    <input
                        type="text"
                        value={identifier}
                        onChange={(event) => setIdentifier(event.target.value)}
                        required
                    />
                </div>

                <div>
                    <label>Password</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(event) => setPassword(event.target.value)}
                        required
                    />
                </div>

                {error && <p>{error}</p>}

                <button type="submit">Login</button>
            </form>

            <p>
                Don't have an account?{" "}
                <Link to="/register">Register</Link>
            </p>
        </div>
    );
}

export default LoginPage;