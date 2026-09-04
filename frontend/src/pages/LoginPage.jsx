import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function LoginPage() {
    const [identifier, setIdentifier] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const [showPassword, setShowPassword] = useState(false);

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
    <div className="auth-page">
        <section className="auth-brand">
            <div className="brand-badge">CMS</div>

            <h1>Contact Management 
                <span> System</span>
            </h1>

            <p className="brand-description">
                Organize and manage your contacts, email addresses,
                and phone numbers in one secure place.
            </p>

            <div className="brand-footer">
                Simple • Secure • Organized
            </div>
        </section>

        <section className="auth-panel">
            <div className="auth-card">
                <div className="auth-heading">
                    <p className="auth-eyebrow">WELCOME BACK</p>

                    <h2>Sign in to your account</h2>

                    <p>
                        Enter your details to access your contact dashboard.
                    </p>
                </div>

                <form className="auth-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Email or Phone Number</label>

                        <input
                            type="text"
                            placeholder="Enter email or phone number"
                            value={identifier}
                            onChange={(event) =>
                                setIdentifier(event.target.value)
                            }
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Password</label>

                        <div className="password-input-wrapper">
    <input
        type={showPassword ? "text" : "password"}
        placeholder="Enter your password"
        value={password}
        onChange={(event) =>
            setPassword(event.target.value)
        }
        required
    />

    <button
        type="button"
        className="password-toggle"
        onClick={() => setShowPassword(!showPassword)}
    >
        {showPassword ? "Hide" : "Show"}
    </button>
</div>
                    </div>

                    {error && (
                        <p className="form-message error-message">
                            {error}
                        </p>
                    )}

                    <button
                        className="primary-button full-width"
                        type="submit"
                    >
                        Sign In
                    </button>
                </form>

                <p className="auth-switch">
                    Don't have an account?{" "}
                    <Link to="/register">Create an account</Link>
                </p>
            </div>
        </section>
    </div>
    );

}

export default LoginPage;