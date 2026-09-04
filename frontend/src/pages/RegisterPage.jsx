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
    <div className="auth-page">
        <section className="auth-brand">
            <div className="brand-badge">CMS</div>

            <h1>
                Contact Management
                <span> System</span>
            </h1>

            <p className="brand-description">
                Create your account and start organizing your contacts,
                email addresses, and phone numbers in one secure place.
            </p>

            <div className="brand-footer">
                Simple • Secure • Organized
            </div>
        </section>

        <section className="auth-panel">
            <div className="auth-card">
                <div className="auth-heading">
                    <p className="auth-eyebrow">
                        GET STARTED
                    </p>

                    <h2>Create your account</h2>

                    <p>
                        Enter your details to start managing your contacts.
                    </p>
                </div>

                <form
                    className="auth-form"
                    onSubmit={handleSubmit}
                >
                    <div className="form-group">
                        <label>Full Name</label>

                        <input
                            type="text"
                            placeholder="Enter your full name"
                            value={fullName}
                            onChange={(event) =>
                                setFullName(event.target.value)
                            }
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Email</label>

                        <input
                            type="email"
                            placeholder="Enter your email"
                            value={email}
                            onChange={(event) =>
                                setEmail(event.target.value)
                            }
                        />
                    </div>

                    <div className="form-group">
                        <label>Phone Number</label>

                        <input
                            type="text"
                            placeholder="Enter your phone number"
                            value={phoneNumber}
                            onChange={(event) =>
                                setPhoneNumber(event.target.value)
                            }
                        />
                    </div>

                    <div className="form-group">
                        <label>Password</label>

                        <input
                            type="password"
                            placeholder="Minimum 8 characters"
                            value={password}
                            onChange={(event) =>
                                setPassword(event.target.value)
                            }
                            minLength="8"
                            required
                        />
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
                        Create Account
                    </button>
                </form>

                <p className="auth-switch">
                    Already have an account?{" "}
                    <Link to="/">
                        Sign in
                    </Link>
                </p>
            </div>
        </section>
    </div>
);

}
export default RegisterPage;
