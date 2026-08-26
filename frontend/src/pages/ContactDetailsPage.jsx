import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

function ContactDetailsPage() {
    const [contact, setContact] = useState(null);
    const [error, setError] = useState("");

    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        async function loadContact() {
            const token = localStorage.getItem("token");

            if (!token) {
                navigate("/");
                return;
            }

            try {
                const response = await fetch("/api/contacts/" + id, {
                    headers: {
                        Authorization: "Bearer " + token,
                    },
                });

                if (!response.ok) {
                    setError("Unable to load contact");
                    return;
                }

                const data = await response.json();
                setContact(data);
            } catch {
                setError("Unable to connect to the server");
            }
        }

        loadContact();
    }, [id, navigate]);

    return (
        <div>
            <h1>Contact Details</h1>

            {error && <p>{error}</p>}

            {contact && (
                <div>
                    <h2>
                        {contact.firstName} {contact.lastName}
                    </h2>
                    <p>{contact.title}</p>
                </div>
            )}
        </div>
    );
}

export default ContactDetailsPage;