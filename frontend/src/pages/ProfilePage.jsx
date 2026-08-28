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

    return (
        <DashboardLayout
            title="Profile"
            subtitle="View and manage your account information."
        >
            {error && <p>{error}</p>}

            {profile && (
    <div>
        {!isEditing ? (
            <>
                <button
                    type="button"
                    onClick={handleStartEdit}
                >
                    Edit Profile
                </button>

                <p>Full Name: {profile.fullName}</p>
                <p>Email: {profile.email || "Not provided"}</p>
                <p>
                    Phone Number:{" "}
                    {profile.phoneNumber || "Not provided"}
                </p>
            </>
        ) : (
            <form onSubmit={handleSaveProfile}>
                <div>
                    <label>Full Name</label>
                    <input
                        type="text"
                        value={editFullName}
                        onChange={(event) =>
                            setEditFullName(event.target.value)
                        }
                    />
                </div>

                <div>
                    <label>Email</label>
                    <input
                        type="email"
                        value={editEmail}
                        onChange={(event) =>
                            setEditEmail(event.target.value)
                        }
                    />
                </div>

                <div>
                    <label>Phone Number</label>
                    <input
                        type="text"
                        value={editPhoneNumber}
                        onChange={(event) =>
                            setEditPhoneNumber(event.target.value)
                        }
                    />
                </div>

                <button type="submit">
                    Save Changes
                </button>

                <button
                    type="button"
                    onClick={() => setIsEditing(false)}
                >
                    Cancel
                </button>
            </form>
        )}
    </div>
)}
        </DashboardLayout>
    );
}

export default ProfilePage;