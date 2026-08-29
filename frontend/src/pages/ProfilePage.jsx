import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

function ProfilePage() {
    const [profile, setProfile] = useState(null);
    const [error, setError] = useState("");

    const [isEditing, setIsEditing] = useState(false);
    const [editFullName, setEditFullName] = useState("");
    const [editEmail, setEditEmail] = useState("");
    const [editPhoneNumber, setEditPhoneNumber] = useState("");

    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [passwordMessage, setPasswordMessage] = useState(""); 

    const navigate = useNavigate();

    useEffect(() => {
        async function loadProfile() {
            const token = localStorage.getItem("token");

            if (!token) {
                navigate("/");
                return;
            }

            try {
                const response = await fetch("/api/auth/profile", {
                    headers: {
                        Authorization: "Bearer " + token,
                    },
                });

                if (response.status === 401 || response.status === 403) {
                    localStorage.removeItem("token");
                    navigate("/");
                    return;
                }

                if (!response.ok) {
                    setError("Unable to load profile");
                    return;
                }

                const data = await response.json();
                setProfile(data);
            } catch {
                setError("Unable to connect to the server");
            }
        }

        loadProfile();
    }, [navigate]);

    function handleStartEdit() {
        setEditFullName(profile.fullName);
        setEditEmail(profile.email || "");
        setEditPhoneNumber(profile.phoneNumber || "");
        setIsEditing(true);
    }

    async function handleSaveProfile(event) {
        event.preventDefault();

        const token = localStorage.getItem("token");

        try {
            const response = await fetch("/api/auth/profile", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + token,
                },
                body: JSON.stringify({fullName: editFullName, email: editEmail, phoneNumber: editPhoneNumber}),
            });

            if (response.status === 401 || response.status === 403) {
                localStorage.removeItem("token");
                navigate("/");
                return;
            }

            if (!response.ok) {
                setError("Unable to update profile");
                return;
            }

            const updatedProfile = await response.json();

            setProfile(updatedProfile);
            setIsEditing(false);
            setError("");
        } catch {
            setError("Unable to connect to the server");
        }
    }

    async function handleChangePassword(event) {
    event.preventDefault();

    const token = localStorage.getItem("token");

    try {
        const response = await fetch("/api/auth/change-password", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + token,
            },
            body: JSON.stringify({
                currentPassword: currentPassword,
                newPassword: newPassword,
            }),
        });

        if (!response.ok) {
            setPasswordMessage("Unable to change password");
            return;
        }

        setCurrentPassword("");
        setNewPassword("");
        setPasswordMessage("Password changed successfully");
    } catch {
        setPasswordMessage("Unable to connect to the server");
    }
}

    return (
        <DashboardLayout
            title="Profile"
            subtitle="View and manage your account information."
        >
            {error && <p>{error}</p>}

            {profile && (
    <section className="profile-card">
        {!isEditing ? (
            <>
            <div className="profile-summary">
    <div className="profile-avatar">
        {profile.fullName
            .split(" ")
            .map((name) => name[0])
            .slice(0, 2)
            .join("")
            .toUpperCase()}
    </div>

    <div className="profile-summary-text">
        <h2>{profile.fullName}</h2>
        <p>{profile.email || "No email provided"}</p>
    </div>
</div>
                <button type="button" 
                className="profile-edit-button"
                onClick={handleStartEdit}>
                    Edit Profile
                </button>

                <div className="profile-details-grid">
    <div className="profile-detail">
        <span className="profile-detail-label">
            Full Name
        </span>

        <p>{profile.fullName}</p>
    </div>

    <div className="profile-detail">
        <span className="profile-detail-label">
            Email
        </span>

        <p>{profile.email || "Not provided"}</p>
    </div>

    <div className="profile-detail">
        <span className="profile-detail-label">
            Phone Number
        </span>

        <p>{profile.phoneNumber || "Not provided"}</p>
    </div>
</div>
            </>
        ) : (
            <form className="profile-edit-form"
            onSubmit={handleSaveProfile}>
                <div className="profile-edit-group">
                    <label>Full Name</label>
                    <input
                        type="text"
                        value={editFullName}
                        onChange={(event) =>
                            setEditFullName(event.target.value)
                        }
                    />
                </div>

                <div className="profile-edit-group"> 
                    <label>Email</label>
                    <input
                        type="email"
                        value={editEmail}
                        onChange={(event) =>
                            setEditEmail(event.target.value)
                        }
                    />
                </div>

                <div className="profile-edit-group">
                    <label>Phone Number</label>
                    <input
                        type="text"
                        value={editPhoneNumber}
                        onChange={(event) =>
                            setEditPhoneNumber(event.target.value)
                        }
                    />
                </div>

                <div className="profile-edit-actions">
                <button
                    className="profile-save-button"
                    type="submit"
                >
                    Save Changes
                </button>

                <button
                className="profile-cancel-button"
                type="button"
                onClick={() => setIsEditing(false)}
                >
                    Cancel
                </button>
                </div>
            </form>
        )}
    </section>
)}

<section className="security-card">
    <div className="security-card-header">
        <p className="security-eyebrow">
            SECURITY
        </p>

        <h2>Change Password</h2>

        <p>
            Update the password used to access your account.
        </p>
    </div>

    <form
        className="security-form"
        onSubmit={handleChangePassword}
    >
        <div className="security-form-group">
            <label>Current Password</label>

            <input
                type="password"
                value={currentPassword}
                onChange={(event) =>
                    setCurrentPassword(event.target.value)
                }
                required
            />
        </div>

        <div className="security-form-group">
            <label>New Password</label>

            <input
                type="password"
                value={newPassword}
                onChange={(event) =>
                    setNewPassword(event.target.value)
                }
                minLength="8"
                required
            />
        </div>

        <button
            className="security-button"
            type="submit"
        >
            Change Password
        </button>
    </form>

    {passwordMessage && (
        <p className="security-message">
            {passwordMessage}
        </p>
    )}
</section>
        </DashboardLayout>
    );
}

export default ProfilePage;