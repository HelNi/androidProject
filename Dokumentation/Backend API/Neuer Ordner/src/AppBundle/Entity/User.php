<?php
/**
 * User: nsh
 * Date: 07.07.2017
 * Time: 10:47
 */

namespace AppBundle\Entity;


use Doctrine\ORM\Mapping as ORM;
use JMS\Serializer\Annotation as Serializer;
use Nelmio\ApiDocBundle\Annotation\ApiDoc;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * Class User
 * @package AppBundle\Entity
 * @ORM\Entity()
 * @ORM\Table(name="api_user")
 */
class User extends \FOS\UserBundle\Model\User
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="AUTO")
     * @ORM\Column(type="integer")
     */
    protected $id;
    /**
     * @ORM\Column(type="string")
     * @Serializer\Exclude()
     */
    protected $currentApiKey = '';

    /**
     * @ORM\Column(type="string", name="first_name")
     * @Assert\NotBlank(message="user.firstName.NotBlank", groups={"Registration", "Profile"})
     * @Assert\Length(min="3", max="5", groups={"Registration", "Profile"})
     */
    protected $firstName;

    /**
     * @ORM\Column(type="string", name="last_name")
     * @Assert\NotBlank(groups={"Registration", "Profile"})
     * @Assert\Length(min="3", max="255", groups={"Registration", "Profile"})
     */
    protected $lastName;

    /**
     * @ORM\Column(type="string", name="salutation")
     * @Assert\NotBlank(groups={"Registration", "Profile"})
     * @Assert\Length(min="3", max="255", groups={"Registration", "Profile"})
     */
    protected $salutation;

    /**
     * @ORM\Column(type="string", name="title")
     * @Assert\Length(min="2", max="255", groups={"Registration", "Profile"})
     */
    protected $title;

    /**
     * @return mixed
     */
    public function getFirstName()
    {
        return $this->firstName;
    }

    /**
     * @param mixed $firstName
     */
    public function setFirstName($firstName)
    {
        $this->firstName = $firstName;
    }

    /**
     * @return mixed
     */
    public function getLastName()
    {
        return $this->lastName;
    }

    /**
     * @param mixed $lastName
     */
    public function setLastName($lastName)
    {
        $this->lastName = $lastName;
    }

    /**
     * @return mixed
     */
    public function getSalutation()
    {
        return $this->salutation;
    }

    /**
     * @param mixed $salutation
     */
    public function setSalutation($salutation)
    {
        $this->salutation = $salutation;
    }

    /**
     * @return mixed
     */
    public function getTitle()
    {
        return $this->title;
    }

    /**
     * @param mixed $title
     */
    public function setTitle($title)
    {
        $this->title = $title;
    }

    /**
     * @return mixed
     */
    public function getCurrentApiKey()
    {
        return $this->currentApiKey;
    }

    /**
     * @param mixed $currentApiKey
     * @return User
     */
    public function setCurrentApiKey($currentApiKey)
    {
        $this->currentApiKey = $currentApiKey;

        return $this;
    }

    /**
     * @return mixed
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * @param mixed $id
     * @return User
     */
    public function setId($id)
    {
        $this->id = $id;

        return $this;
    }

    /**
     * {@inheritdoc}
     */
    public function serialize()
    {
        return serialize(array(
            $this->password,
            $this->salt,
            $this->usernameCanonical,
            $this->username,
            $this->enabled,
            $this->id,
            $this->email,
            $this->emailCanonical,
            $this->salutation,
            $this->title,
            $this->firstName,
            $this->lastName
        ));
    }

    /**
     * {@inheritdoc}
     */
    public function unserialize($serialized)
    {
        $data = unserialize($serialized);

        list(
            $this->password,
            $this->salt,
            $this->usernameCanonical,
            $this->username,
            $this->enabled,
            $this->id,
            $this->email,
            $this->emailCanonical,
            $this->salutation,
            $this->title,
            $this->firstName,
            $this->lastName
            ) = $data;
    }
}