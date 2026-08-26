import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function RegisterPage() {
    const [fullName, setFullName] = useState("");
    const [email, setEmail] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const navigate = useNavigate();

    async function handleSubmit(event) {
        event.preventDefault();
        setError("");

        if (email === "" && phoneNumber === "") {
            setError("Please enter an email or phone number");
            return;
        }

        try {
            const response = await fetch("/api/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    fullName: fullName,
                    email: email,
                    phoneNumber: phoneNumber,
                    password: password,
                }),
            });

            if (!response.ok) {
                setError("Registration failed");
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
            <h1>Register</h1>

            <form onSubmit={handleSubmit}>
                <div>
                    <label>Full Name</label>
                    <input
                        type="text"
                        value={fullName}
                        onChange={(event) => setFullName(event.target.value)}
                        required
                    />
                </div>

                <div>
                    <label>Email</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(event) => setEmail(event.target.value)}
                    />
                </div>

                <div>
                    <label>Phone Number</label>
                    <input
                        type="text"
                        value={phoneNumber}
                        onChange={(event) => setPhoneNumber(event.target.value)}
                    />
                </div>

                <div>
                    <label>Password</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(event) => setPassword(event.target.value)}
                        minLength="8"
                        required
                    />
                </div>

                {error && <p>{error}</p>}

                <button type="submit">Register</button>
            </form>

            <p>
                Already have an account?{" "}
                <Link to="/">Login</Link>
            </p>
        </div>
    );
}

export default RegisterPage;
