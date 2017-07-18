<?php
/**
 * User: nsh
 * Date: 11.07.2017
 * Time: 10:15
 */

namespace AppBundle\Security\Voter;

use AppBundle\Entity\Entry;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Authorization\AccessDecisionManagerInterface;
use Symfony\Component\Security\Core\Authorization\Voter\Voter;
/**
 * User: nsh
 * Date: 11.07.2017
 * Time: 10:05
 */
class EntryVoter extends Voter
{
    const EDIT = 'EDIT';
    const DELETE = 'DELETE';
    const VIEW = 'VIEW';
    /**
     * @var AccessDecisionManagerInterface
     */
    private $decisionManager;

    /**
     * EntryVoter constructor.
     */
    public function __construct(AccessDecisionManagerInterface $decisionManager)
    {
        $this->decisionManager = $decisionManager;
    }

    /**
     * Determines if the attribute and subject are supported by this voter.
     *
     * @param string $attribute An attribute
     * @param mixed $subject The subject to secure, e.g. an object the user wants to access or any other PHP type
     *
     * @return bool True if the attribute and subject are supported, false otherwise
     */
    protected function supports($attribute, $subject)
    {
        if (!$subject instanceof Entry) {
            return false;
        }

        switch ($attribute) {
            case self::EDIT:
            case self::VIEW:
            case self::DELETE:
                return \true;
        }

        return \false;
    }

    /**
     * Perform a single access check operation on a given attribute, subject and token.
     * It is safe to assume that $attribute and $subject already passed the "supports()" method check.
     *
     * @param string $attribute
     * @param Entry $subject
     * @param TokenInterface $token
     *
     * @return bool
     */
    protected function voteOnAttribute(
        $attribute,
        $subject,
        TokenInterface $token
    ) {
        switch ($attribute) {
            // Basically the owner of an entry and an admin may do everything. Normal users don't.
            case self::VIEW:
            case self::EDIT:
            case self::DELETE:
                return $subject->getUser() === $token->getUser() || $this->decisionManager->decide(
                        $token,
                        ['ROLE_ADMIN'],
                        $subject
                    );
        }
        return \false;
    }
}